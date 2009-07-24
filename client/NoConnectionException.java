/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package machat.client;

import java.io.IOException;

/**
 * Exception thrown when the connection fails.
 * @author Macha <macha.hack@gmail.com>
 */
public class NoConnectionException extends IOException {

    /**
     * Creates a new instance of <code>NoConnectionException</code> without detail message.
     */
    public NoConnectionException() {
    }


    /**
     * Constructs an instance of <code>NoConnectionException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public NoConnectionException(String msg) {
        super(msg);
    }
}
