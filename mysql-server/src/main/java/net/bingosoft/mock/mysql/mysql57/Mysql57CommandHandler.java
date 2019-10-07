package net.bingosoft.mock.mysql.mysql57;

import net.bingosoft.mock.mysql.Authentication;
import net.bingosoft.mock.mysql.CommandHandler;
import net.bingosoft.mock.mysql.protocol.MysqlPacket;
import net.bingosoft.mock.mysql.protocol.datatype.pint.PInt;
import net.bingosoft.mock.mysql.protocol.datatype.pstring.PString;
import net.bingosoft.mock.mysql.protocol.datatype.pstring.StrLenenc;
import net.bingosoft.mock.mysql.protocol.support.*;
import net.bingosoft.mock.mysql.protocol.utils.HexUtil;
import net.bingosoft.mock.mysql.protocol.v10.MysqlPacketFactory;
import net.bingosoft.mock.mysql.protocol.v10.payload.*;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;

import java.io.IOException;
import java.net.Socket;
import java.sql.*;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author kael.
 */
public class Mysql57CommandHandler implements CommandHandler {
    private static final Logger log             = Logger.getLogger(Mysql57CommandHandler.class.getName());
    private static final String T_VARIABLES     = "t_variables";
    private static final String VARIABLES_NAME  = "Variable_name";
    private static final String VARIABLES_VALUE = "Value";

    private static final String T_COLLATION      = "t_collation";
    private static final String COLLATION_COLUMN = "Collation";
    private static final String CHARSET_COLUMN   = "Charset";
    private static final String ID_COLUMN        = "id";
    private static final String DEFAULT_COLUMN   = "Default";
    private static final String COMPILED_COLUMN  = "Compiled";
    private static final String SORTLEN_COLUMN   = "Sortlen";


    private Connection                conn;
    private Map<String, String>       initVariables  = new HashMap<>();
    private List<Map<String, String>> initCollations = new ArrayList<>();

    public Mysql57CommandHandler() {
        try {
            conn = DriverManager.getConnection("jdbc:h2:mem:test", "sa", "sa");
            executeSql("CREATE TABLE " + T_VARIABLES + "  ( id varchar(38) NOT NULL, " + VARIABLES_NAME + " varchar(255) NULL, " + VARIABLES_VALUE + " varchar(255) NULL, PRIMARY KEY (`id`));");
            executeSql("CREATE TABLE "+T_COLLATION+"  (Collation varchar(255) NULL,Charset varchar(255) NULL,id  varchar(255) not NULL,Default varchar(255) NULL,Compiled varchar(255) NULL,Sortlen varchar(255) NULL,PRIMARY KEY (id));");
            initVariables();
            initCollation();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean handle(Socket socket, MysqlPacket<CommandPayload> mp, Authentication authentication) throws IOException {
        Command cmd = mp.getPayload().getCommand();
        switch (cmd) {
            case COM_QUERY:
                Statement stmt;
                Processor processor = new Processor(authentication);
                String sql = new String(mp.getPayload().toByteArray());
                try {
                    stmt = CCJSqlParserUtil.parse(sql, parser -> {
                        if (isShowVariables(parser)) {
                            StringBuilder sb = new StringBuilder(sql);
                            String nsql = sb.replace(parser.getToken(2).absoluteBegin - 1, parser.getToken(2).absoluteEnd - 1, VARIABLES_NAME + ", " + VARIABLES_VALUE + " FROM " + T_VARIABLES)
                                    .replace(parser.getToken(1).absoluteBegin - 1, parser.getToken(1).absoluteEnd - 1, "SELECT")
                                    .toString();
                            parser.ReInit(nsql);
                            processor.markShowVariable();
                        } else if (isSelectAutoIncrement(parser)) {
                            parser.ReInit("select " + VARIABLES_VALUE + ", " + VARIABLES_NAME + " from " + T_VARIABLES + " WHERE " + VARIABLES_NAME + "= '@@session.auto_increment_increment'");
                            processor.markSelectAutoIncrement();
                        } else if (isShowCollation(parser)) {
                            String nsql = "SELECT " + COLLATION_COLUMN + ", " + CHARSET_COLUMN + ", " + ID_COLUMN + ", "
                                    + DEFAULT_COLUMN + ", " + COMPILED_COLUMN + ", " + SORTLEN_COLUMN + " FROM " + T_COLLATION;
                            parser.ReInit(nsql);
                            processor.markShowCollation();
                        }else if (isSetCommand(parser)){
                            processor.markSetCommand();
                        } else {

                        }
                    });
                } catch (JSQLParserException ex) {
                    throw new RuntimeException(ex);
                }
                processor.process(socket, stmt);
                break;
            case COM_QUIT:
                return false;
            default:
                break;
        }
        
        return true;
    }

    private class Processor {
        private int            type = 0;
        private Authentication authentication;

        public Processor(Authentication authentication) {
            this.authentication = authentication;
        }

        public void markShowVariable() {
            type = 1;
        }

        public void markSelectAutoIncrement() {
            type = 2;
        }

        public void markShowCollation() {
            type = 3;
        }
        
        public void markSetCommand(){
            type = 4;
        }
        
        public boolean isShowVariable() {
            return type == 1;
        }

        public boolean isSelectAutoIncrement() {
            return type == 2;
        }

        public boolean isShowCollation() {
            return type == 3;
        }

        public boolean isSetCommand(){
            return type == 4;
        }
        
        public void process(Socket socket, Statement stmt) throws IOException {
            if (isShowVariable()) {
                processShowVariables(socket, stmt);
            } else if (isSelectAutoIncrement()) {
                processSelectAutoIncrement(socket, stmt);
            } else if (isShowCollation()) {
                processShowCollection(socket, stmt);
            } else if (isSetCommand()){
                processSetCommand(socket, stmt);
            }else {
                List<Map<String, Object>> rs = executeQuery(stmt.toString());
                System.out.println(rs);
            }
        }

        private void processShowVariables(Socket socket, Statement stmt) throws IOException {
            List<Map<String, Object>> rs = executeQuery(stmt.toString());
            ColumnCount columnCount = new ColumnCount(2);
            socket.getOutputStream().write(MysqlPacketFactory.withPayload(columnCount).toByteArray());
            ColumnDefinition41 cd1 = ColumnDefinition41.Builder.create()
                    .withSchema(PString.strLenenc(authentication.getDatabase().getBytes()))
                    .withTable(PString.strLenenc(T_VARIABLES.getBytes()))
                    .withOrgTable(PString.strLenenc(T_VARIABLES.getBytes()))
                    .withName(PString.strLenenc(VARIABLES_NAME.getBytes()))
                    .withOrgName(PString.strLenenc(VARIABLES_NAME.getBytes()))
                    .withCharacterSet(CharacterSet.UTF8_GENERAL_CI)
                    .withColumnLength(PInt.int4(100))
                    .withType(ColumnType.MYSQL_TYPE_VARCHAR)
                    .withDecimals(ColumnDecimals.STATIC_STRING)
                    .build();
            socket.getOutputStream().write(MysqlPacketFactory.withPayload(cd1).toByteArray());
            ColumnDefinition41 cd2 = ColumnDefinition41.Builder.create()
                    .withSchema(PString.strLenenc(authentication.getDatabase().getBytes()))
                    .withTable(PString.strLenenc(T_VARIABLES.getBytes()))
                    .withOrgTable(PString.strLenenc(T_VARIABLES.getBytes()))
                    .withName(PString.strLenenc(VARIABLES_NAME.getBytes()))
                    .withOrgName(PString.strLenenc(VARIABLES_NAME.getBytes()))
                    .withCharacterSet(CharacterSet.UTF8_GENERAL_CI)
                    .withColumnLength(PInt.int4(100))
                    .withType(ColumnType.MYSQL_TYPE_VARCHAR)
                    .withDecimals(ColumnDecimals.STATIC_STRING)
                    .build();
            socket.getOutputStream().write(MysqlPacketFactory.withPayload(cd2).toByteArray());

            socket.getOutputStream().write(MysqlPacketFactory.withPayload(new EOF()).toByteArray());

            rs.forEach(map -> {

            });

            socket.getOutputStream().write(MysqlPacketFactory.withPayload(new EOF()).toByteArray());
        }

        private void processSelectAutoIncrement(Socket socket, Statement stmt) throws IOException {
            List<Map<String, Object>> rs = executeQuery(stmt.toString());
            String key = rs.get(0).entrySet().iterator().next().getKey();
            Object value = rs.get(0).entrySet().iterator().next().getValue();
            ColumnCount columnCount = new ColumnCount(1);
            socket.getOutputStream().write(MysqlPacketFactory.withPayload(columnCount).toByteArray());
            ColumnDefinition41 cd1 = ColumnDefinition41.Builder.create()
                    .withSchema(PString.strLenenc(authentication.getDatabase().getBytes()))
                    .withTable(PString.strLenenc(T_VARIABLES.getBytes()))
                    .withOrgTable(PString.strLenenc(T_VARIABLES.getBytes()))
                    .withName(PString.strLenenc(key.getBytes()))
                    .withOrgName(PString.strLenenc(key.getBytes()))
                    .withCharacterSet(CharacterSet.UTF8_GENERAL_CI)
                    .withColumnLength(PInt.int4(100))
                    .withType(ColumnType.MYSQL_TYPE_VARCHAR)
                    .withDecimals(ColumnDecimals.STATIC_STRING)
                    .build();

            socket.getOutputStream().write(MysqlPacketFactory.withPayload(cd1).toByteArray());
            socket.getOutputStream().write(MysqlPacketFactory.withPayload(new EOF()).toByteArray());
            ResultsetRow row = new ResultsetRow(Collections.singletonList(PString.strLenenc(String.valueOf(value).getBytes())));
            socket.getOutputStream().write(MysqlPacketFactory.withPayload(row).toByteArray());
            socket.getOutputStream().write(MysqlPacketFactory.withPayload(new EOF()).toByteArray());

        }

        private void processShowCollection(Socket socket, Statement stmt) throws IOException {
            List<Map<String, Object>> rs = executeQuery(stmt.toString());
            ColumnCount columnCount = new ColumnCount(6);
            socket.getOutputStream().write(MysqlPacketFactory.withPayload(columnCount).toByteArray());
            ColumnDefinition41 cd1 = ColumnDefinition41.Builder.create()
                    .withSchema(PString.strLenenc(authentication.getDatabase().getBytes()))
                    .withTable(PString.strLenenc(T_COLLATION.getBytes()))
                    .withOrgTable(PString.strLenenc(T_COLLATION.getBytes()))
                    .withName(PString.strLenenc(COLLATION_COLUMN.getBytes()))
                    .withOrgName(PString.strLenenc(COLLATION_COLUMN.getBytes()))
                    .withCharacterSet(CharacterSet.UTF8_GENERAL_CI)
                    .withColumnLength(PInt.int4(100))
                    .withType(ColumnType.MYSQL_TYPE_VARCHAR)
                    .withDecimals(ColumnDecimals.STATIC_STRING)
                    .build();
            socket.getOutputStream().write(MysqlPacketFactory.withPayload(cd1).toByteArray());

            ColumnDefinition41 cd2 = ColumnDefinition41.Builder.create()
                    .withSchema(PString.strLenenc(authentication.getDatabase().getBytes()))
                    .withTable(PString.strLenenc(T_COLLATION.getBytes()))
                    .withOrgTable(PString.strLenenc(T_COLLATION.getBytes()))
                    .withName(PString.strLenenc(CHARSET_COLUMN.getBytes()))
                    .withOrgName(PString.strLenenc(CHARSET_COLUMN.getBytes()))
                    .withCharacterSet(CharacterSet.UTF8_GENERAL_CI)
                    .withColumnLength(PInt.int4(100))
                    .withType(ColumnType.MYSQL_TYPE_VARCHAR)
                    .withDecimals(ColumnDecimals.STATIC_STRING)
                    .build();
            socket.getOutputStream().write(MysqlPacketFactory.withPayload(cd2).toByteArray());

            ColumnDefinition41 cd3 = ColumnDefinition41.Builder.create()
                    .withSchema(PString.strLenenc(authentication.getDatabase().getBytes()))
                    .withTable(PString.strLenenc(T_COLLATION.getBytes()))
                    .withOrgTable(PString.strLenenc(T_COLLATION.getBytes()))
                    .withName(PString.strLenenc(ID_COLUMN.getBytes()))
                    .withOrgName(PString.strLenenc(ID_COLUMN.getBytes()))
                    .withCharacterSet(CharacterSet.UTF8_GENERAL_CI)
                    .withColumnLength(PInt.int4(100))
                    .withType(ColumnType.MYSQL_TYPE_LONGLONG)
                    .withDecimals(ColumnDecimals.STATIC_STRING)
                    .build();
            socket.getOutputStream().write(MysqlPacketFactory.withPayload(cd3).toByteArray());

            ColumnDefinition41 cd4 = ColumnDefinition41.Builder.create()
                    .withSchema(PString.strLenenc(authentication.getDatabase().getBytes()))
                    .withTable(PString.strLenenc(T_COLLATION.getBytes()))
                    .withOrgTable(PString.strLenenc(T_COLLATION.getBytes()))
                    .withName(PString.strLenenc(DEFAULT_COLUMN.getBytes()))
                    .withOrgName(PString.strLenenc(DEFAULT_COLUMN.getBytes()))
                    .withCharacterSet(CharacterSet.UTF8_GENERAL_CI)
                    .withColumnLength(PInt.int4(100))
                    .withType(ColumnType.MYSQL_TYPE_VARCHAR)
                    .withDecimals(ColumnDecimals.STATIC_STRING)
                    .build();
            socket.getOutputStream().write(MysqlPacketFactory.withPayload(cd4).toByteArray());

            ColumnDefinition41 cd5 = ColumnDefinition41.Builder.create()
                    .withSchema(PString.strLenenc(authentication.getDatabase().getBytes()))
                    .withTable(PString.strLenenc(T_COLLATION.getBytes()))
                    .withOrgTable(PString.strLenenc(T_COLLATION.getBytes()))
                    .withName(PString.strLenenc(COMPILED_COLUMN.getBytes()))
                    .withOrgName(PString.strLenenc(COMPILED_COLUMN.getBytes()))
                    .withCharacterSet(CharacterSet.UTF8_GENERAL_CI)
                    .withColumnLength(PInt.int4(100))
                    .withType(ColumnType.MYSQL_TYPE_VARCHAR)
                    .withDecimals(ColumnDecimals.STATIC_STRING)
                    .build();
            socket.getOutputStream().write(MysqlPacketFactory.withPayload(cd5).toByteArray());

            ColumnDefinition41 cd6 = ColumnDefinition41.Builder.create()
                    .withSchema(PString.strLenenc(authentication.getDatabase().getBytes()))
                    .withTable(PString.strLenenc(T_COLLATION.getBytes()))
                    .withOrgTable(PString.strLenenc(T_COLLATION.getBytes()))
                    .withName(PString.strLenenc(SORTLEN_COLUMN.getBytes()))
                    .withOrgName(PString.strLenenc(SORTLEN_COLUMN.getBytes()))
                    .withCharacterSet(CharacterSet.UTF8_GENERAL_CI)
                    .withColumnLength(PInt.int4(100))
                    .withType(ColumnType.MYSQL_TYPE_VARCHAR)
                    .withDecimals(ColumnDecimals.STATIC_STRING)
                    .build();
            socket.getOutputStream().write(MysqlPacketFactory.withPayload(cd6).toByteArray());
            socket.getOutputStream().write(MysqlPacketFactory.withPayload(new EOF()).toByteArray());
            List<ResultsetRow> sl = rs.stream().map(map -> {
                StrLenenc collation = PString.strLenenc(map.get(COLLATION_COLUMN.toUpperCase()).toString().getBytes());
                StrLenenc charset = PString.strLenenc(map.get(CHARSET_COLUMN.toUpperCase()).toString().getBytes());
                StrLenenc id = PString.strLenenc(map.get(ID_COLUMN.toUpperCase()).toString().getBytes());
                StrLenenc def = PString.strLenenc(map.get(DEFAULT_COLUMN.toUpperCase()).toString().getBytes());
                StrLenenc compiled = PString.strLenenc(map.get(COMPILED_COLUMN.toUpperCase()).toString().getBytes());
                StrLenenc sortlen = PString.strLenenc(map.get(SORTLEN_COLUMN.toUpperCase()).toString().getBytes());
                List<StrLenenc> list = new ArrayList<>();
                list.add(collation);
                list.add(charset);
                list.add(id);
                list.add(def);
                list.add(compiled);
                list.add(sortlen);
                return new ResultsetRow(list);
            }).collect(Collectors.toList());
            sl.forEach(row -> {
                try {
                    socket.getOutputStream().write(MysqlPacketFactory.withPayload(row).toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            socket.getOutputStream().write(MysqlPacketFactory.withPayload(new EOF()).toByteArray());
            
        }
        
        private void processSetCommand(Socket socket, Statement stmt) throws IOException{
            log.info("process " + stmt.toString());
            socket.getOutputStream().write(MysqlPacketFactory.withPayload(new OK()).toByteArray());
        }
    }


    private boolean executeSql(String sql) {
        try {
            log.info("sql: \n" + sql);
            return conn.createStatement().execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected boolean isShowVariables(CCJSqlParser parser) {
        boolean isShow = "SHOW".equalsIgnoreCase(parser.getToken(1).toString());
        boolean isVariable = "VARIABLES".equalsIgnoreCase(parser.getToken(2).toString());
        boolean withWhere = "WHERE".equalsIgnoreCase(parser.getToken(3).toString());
        return isShow && isVariable && withWhere;
    }

    protected boolean isSelectAutoIncrement(CCJSqlParser parser) {
        boolean isSelect = "SELECT".equalsIgnoreCase(parser.getToken(1).toString());
        boolean isDoubleAt = "@@".equalsIgnoreCase(parser.getToken(2).toString());
        boolean isSession = "session".equalsIgnoreCase(parser.getToken(3).toString());
        boolean isPoint = ".".equalsIgnoreCase(parser.getToken(4).toString());
        boolean isAutoIncrement = "auto_increment_increment".equalsIgnoreCase(parser.getToken(5).toString());
        return isSelect && isDoubleAt && isSession && isPoint && isAutoIncrement;
    }

    protected boolean isShowCollation(CCJSqlParser parser) {
        boolean isShow = "SHOW".equalsIgnoreCase(parser.getToken(1).toString());
        boolean isCollation = "COLLATION".equalsIgnoreCase(parser.getToken(2).toString());
        return isShow && isCollation;
    }
    
    protected boolean isSetCommand(CCJSqlParser parser){
        boolean isSet = "SET".equalsIgnoreCase(parser.getToken(1).toString());
        // boolean isNames = "NAMES".equalsIgnoreCase(parser.getToken(2).toString());
        return isSet;
    }
    
    private List<Map<String, Object>> executeQuery(String sql) {
        try {
            log.info("sql: \n" + sql);
            List<Map<String, Object>> list = new ArrayList<>();
            ResultSet rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                ResultSetMetaData md = rs.getMetaData();
                int c = md.getColumnCount();
                Map<String, Object> row = new HashMap<>();
                for(int i=1; i <= c;i++){
                    String k = rs.getString(i);
                    row.put(md.getColumnLabel(i), k);
                }
                list.add(row);
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void initVariables() {
        initVariables.put("character_set_client", "utf8mb4");
        initVariables.put("character_set_connection", "utf8mb4");
        initVariables.put("character_set_results", "utf8mb4");
        initVariables.put("character_set_server", "latin1");
        initVariables.put("init_connect", "");
        initVariables.put("interactive_timeout", "28800");
        initVariables.put("lower_case_table_names", "0");
        initVariables.put("max_allowed_packet", "4194304");
        initVariables.put("net_buffer_length", "16384");
        initVariables.put("net_write_timeout", "60");
        initVariables.put("query_cache_size", "1048576");
        initVariables.put("query_cache_type", "OFF");
        initVariables.put("sql_mode", "ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION");
        initVariables.put("system_time_zone", "UTC");
        initVariables.put("time_zone", "SYSTEM");
        initVariables.put("transaction_isolation", "REPEATABLE-READ");
        initVariables.put("tx_isolation", "REPEATABLE-READ");
        initVariables.put("wait_timeout", "28800");
        initVariables.put("@@session.auto_increment_increment", "1");
        initVariables.forEach((k, v) -> executeSql("insert into " + T_VARIABLES + " (id," + VARIABLES_NAME + ", " + VARIABLES_VALUE + ") values ('" + UUID.randomUUID().toString() + "','" + k + "', '" + v + "')"));

    }

    private void initCollation() {
        Map<String, String> initCollation = new HashMap<>();
        initCollation.put(COLLATION_COLUMN, "utf8_general_ci");
        initCollation.put(CHARSET_COLUMN, "utf8");
        initCollation.put(ID_COLUMN, "33");
        initCollation.put(DEFAULT_COLUMN, "Yes");
        initCollation.put(COMPILED_COLUMN, "Yes");
        initCollation.put(SORTLEN_COLUMN, "1");
        initCollations.add(initCollation);

        initCollations.forEach(map -> {
            String sql = "insert into " + T_COLLATION + " (" + COLLATION_COLUMN + ", " + CHARSET_COLUMN + ", " + ID_COLUMN + ", " + DEFAULT_COLUMN + ", " + COMPILED_COLUMN + ", " + SORTLEN_COLUMN + ") VALUES ("
                    + "'" + map.get(COMPILED_COLUMN) + "', '" + map.get(CHARSET_COLUMN) + "', '" + map.get(ID_COLUMN) + "', '" + map.get(DEFAULT_COLUMN) + "', '" + map.get(COMPILED_COLUMN) + "', '" + map.get(SORTLEN_COLUMN) + "')";
            executeSql(sql);
        });
    }

}
