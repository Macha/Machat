/*
 *  Machat
 *
 *  Copyright (C) Macha <macha.hack@gmail.com>
 *
 *  This file is part of Machat.
 *
 *  Machat is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Machat is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Machat.  If not, see <http://www.gnu.org/licenses/>.
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
