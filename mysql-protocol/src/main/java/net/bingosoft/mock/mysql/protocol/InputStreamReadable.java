package net.bingosoft.mock.mysql.protocol;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author kael.
 */
public interface InputStreamReadable<T> {
    
    T read(InputStream is) throws IOException;
}
