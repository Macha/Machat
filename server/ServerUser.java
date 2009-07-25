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
package machat.server;

import machat.lib.MachatUser;

/**
 * This class is used for managing users on the server side.
 * @author Macha <macha.hack@gmail.com>
 */
public class ServerUser extends MachatUser {
    private int connectionId;
    /**
     * Creates a disconnected server user.
     * @param id The user's ID
     * @param username The user's last used username.
     */
    public ServerUser(int id, String username) {
        
    }
    /**
     * Creates a new connected Server side user.
     * @param id The user's ID
     * @param username The username of the user
     * @param connectionId The id of the connection that the user is currently using.
     */
    public ServerUser(int id, String username, int connectionId) {
        // Database user check goes here.
    }

    /**
     * Sends a message to the user.
     * @param message The message to send the user.
     */
    @Override
    public void sendMessage(String message) {
        //TODO Actally send the message
        System.out.println("Command to send message to [" + this.id + "] recieved");
    }
    public static MachatUser fromString(String userString) {
        String[] details = userString.split(":");
        int id = Integer.parseInt(details[0]);
        return new ServerUser(id, details[1]);
    }

}