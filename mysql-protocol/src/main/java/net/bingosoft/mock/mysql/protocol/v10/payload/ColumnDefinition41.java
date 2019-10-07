package net.bingosoft.mock.mysql.protocol.v10.payload;

import net.bingosoft.mock.mysql.protocol.Payload;
import net.bingosoft.mock.mysql.protocol.datatype.pint.*;
import net.bingosoft.mock.mysql.protocol.datatype.pstring.PString;
import net.bingosoft.mock.mysql.protocol.datatype.pstring.StrFix;
import net.bingosoft.mock.mysql.protocol.datatype.pstring.StrLenenc;
import net.bingosoft.mock.mysql.protocol.support.*;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author kael.
 * @see <pre>https://dev.mysql.com/doc/internals/en/com-query-response.html#packet-Protocol::ColumnDefinition41</pre>
 */
public class ColumnDefinition41 implements Payload {

    protected StrLenenc      catalog            = PString.strLenenc("def".getBytes());
    protected StrLenenc      schema             = PString.strLenenc(new byte[0]);
    protected StrLenenc      table              = PString.strLenenc(new byte[0]);
    protected StrLenenc      orgTable           = PString.strLenenc(new byte[0]);
    protected StrLenenc      name               = PString.strLenenc(new byte[0]);
    protected StrLenenc      orgName            = PString.strLenenc(new byte[0]);
    protected IntLenenc      fields             = PInt.lenenc(0x0c);
    protected CharacterSet   characterset       = CharacterSet.UTF8_GENERAL_CI;
    protected Int4           columnLength       = PInt.int4(0);
    protected ColumnType     type               = ColumnType.MYSQL_TYPE_VARCHAR;
    protected Int2           flag               = PInt.int2(0);
    protected ColumnDecimals decimals           = ColumnDecimals.INTEGER;
    protected Int2           filler             = PInt.int2(0);
    protected IntLenenc      defaultValueLength = PInt.lenenc(0);
    protected StrFix         defaultValue       = PString.strFix(new byte[0]);

    @Override
    public byte[] toByteArray() {
        return ByteArray.create()
                .concat(catalog.toByteArray())
                .concat(schema.toByteArray())
                .concat(table.toByteArray())
                .concat(orgTable.toByteArray())
                .concat(name.toByteArray())
                .concat(orgName.toByteArray())
                .concat(fields.toByteArray())
                .concat(characterset.getValue())
                .concat(columnLength.toByteArray())
                .concat(type.getValue())
                .concat(flag.toByteArray())
                .concat(decimals.toByteArray())
                .concat(filler.toByteArray())
                .concat(defaultValueLength.toByteArray())
                .concat(defaultValue.toByteArray())
                .toArray();
    }

    @Override
    public Payload read(InputStream is) throws IOException {
        throw new UnsupportedOperationException();
    }

    public static class Builder {
        private ColumnDefinition41 cd = new ColumnDefinition41();

        private Builder() {

        }

        public static Builder create() {
            return new Builder();
        }

        public Builder withCatalog(StrLenenc catalog) {
            if (null != catalog) {
                cd.catalog = catalog;
            }
            return this;
        }

        public Builder withSchema(StrLenenc schema) {
            if (null != schema) {
                cd.schema = schema;
            }
            return this;
        }

        public Builder withTable(StrLenenc table) {
            if (null != table) {
                cd.table = table;
            }
            return this;
        }

        public Builder withOrgTable(StrLenenc orgTable) {
            if (null != orgTable) {
                cd.orgTable = orgTable;
            }
            return this;
        }

        public Builder withName(StrLenenc name) {
            if (null != name) {
                cd.name = name;
            }
            return this;
        }

        public Builder withOrgName(StrLenenc orgName) {
            if (null != orgName) {
                cd.orgName = orgName;
            }
            return this;
        }

        public Builder withFields(IntLenenc fields) {
            if (null != fields) {
                cd.fields = fields;
            }
            return this;
        }

        public Builder withCharacterSet(CharacterSet characterset) {
            if (null != characterset) {
                cd.characterset = characterset;
            }
            return this;
        }

        public Builder withColumnLength(Int4 columnLength) {
            if (null != columnLength) {
                cd.columnLength = columnLength;
            }
            return this;
        }

        public Builder withType(ColumnType type) {
            if (null != type) {
                cd.type = type;
            }
            return this;
        }

        public Builder withFlag(Int2 flag) {
            if (null != flag) {
                cd.flag = flag;
            }
            return this;
        }

        public Builder withDecimals(ColumnDecimals decimals) {
            if (null != decimals) {
                cd.decimals = decimals;
            }
            return this;
        }

        public Builder withFiller(Int2 filler) {
            if (null != filler) {
                cd.filler = filler;
            }
            return this;
        }

        public Builder withDefaultValue(StrFix defaultValue) {
            if (defaultValue != null) {
                cd.defaultValue = defaultValue;
                cd.defaultValueLength = PInt.lenenc(defaultValue.getLength());
            }
            return this;
        }

        public ColumnDefinition41 build() {
            return cd.clone();
        }
    }

    @Override
    protected ColumnDefinition41 clone() {
        ColumnDefinition41 cd = new ColumnDefinition41();
        cd.catalog = this.catalog.clone();
        cd.schema = this.schema.clone();
        cd.table = this.table.clone();
        cd.orgTable = this.orgTable.clone();
        cd.name = this.name.clone();
        cd.orgName = this.orgName.clone();
        cd.fields = this.fields.clone();
        cd.characterset = this.characterset;
        cd.columnLength = this.columnLength.clone();
        cd.type = this.type;
        cd.flag = this.flag.clone();
        cd.decimals = this.decimals;
        cd.filler = this.filler.clone();
        cd.defaultValueLength = this.defaultValueLength.clone();
        cd.defaultValue = this.defaultValue.clone();
        return cd;
    }
}
