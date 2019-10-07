package net.bingosoft.mock.mysql;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.net.Socket;

/**
 * @author kael.
 */
public interface AuthHandler {
    
    Authentication authenticate(Socket socket) throws AuthenticationException, IOException;
    
}
