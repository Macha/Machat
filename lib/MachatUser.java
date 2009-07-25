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

package machat.lib;

/**
 * The base class for a user.
 * @author Macha <macha.hack@gmail.com>
 */
public abstract class MachatUser {
    protected int id;
    protected String username;
    /**
     * Gets this user's id
     * @return The user's ID
     */
    public int getId() {
        return this.id;
    }
    /**
     * Gets the user's current username.
     * @return The username that the user is currently using
     */
    public String getUsername() {
        return this.username;
    }
    /**
     * Sends a message to this user
     * @param message The message to be sent.
     */
    public abstract void sendMessage(String message);
    @Override
    public String toString() {
        return id + ":" + username;
    }
}
