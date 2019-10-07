package net.bingosoft.mock.mysql.mysql57;

import net.bingosoft.mock.mysql.Authentication;

/**
 * @author kael.
 */
public class SimpleAuthentication implements Authentication {
    protected String username;
    protected String database;

    public SimpleAuthentication(String username, String database) {
        this.username = username;
        this.database = database;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getDatabase() {
        return database;
    }
}
