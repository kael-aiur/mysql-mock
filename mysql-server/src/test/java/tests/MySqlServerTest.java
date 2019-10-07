package tests;

import junit.framework.TestCase;
import net.bingosoft.mock.mysql.MySqlBuilder;
import net.bingosoft.mock.mysql.MysqlServer;
import org.junit.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author kael.
 */
public class MySqlServerTest extends TestCase {
    @Test
    public void testMySqlLogin() throws ClassNotFoundException, SQLException, IOException {
        MysqlServer server = MySqlBuilder.create().withPort(3306).build();
        server.start();
        
        Class.forName("com.mysql.jdbc.Driver");
        String jdbcUrl = "jdbc:mysql://localhost:3306/binlog?useUnicode=true&characterEncoding=UTF-8";
        String username = "root";
        String password = "root";
        Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
        conn.close();
    }
    
}
