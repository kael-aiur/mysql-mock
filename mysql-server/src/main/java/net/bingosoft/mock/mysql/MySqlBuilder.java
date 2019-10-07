package net.bingosoft.mock.mysql;

import net.bingosoft.mock.mysql.mysql57.Mysql57;

import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * @author kael.
 */
public class MySqlBuilder {
    
    private int port;
    
    public static MySqlBuilder create(){
        return new MySqlBuilder();
    }
    
    public MySqlBuilder withPort(int port){
        this.port = port;
        return this;
    }
    
    public MysqlServer build() {
        try {
            return new Mysql57(port);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
}
