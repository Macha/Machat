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

import machat.lib.*;

/**
 * This class represents a user on the client side.
 * @author Macha <macha.hack@gmail.com>
 */
public class ClientUser extends MachatUser {

    /**
     * This constructer should not be called directly, only through ClientUser.fromString()
     * @param id The user's id
     * @param username The user's username
     */
    public ClientUser(int id, String username) {
        this.id = id;
    }

    @Override
    public void sendMessage(String message) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Returns a user based on a string in the format id:username
     * @param userString User details in id:username format.
     * @return The user object
     */
    public static MachatUser fromString(String userString) {
        String[] details = userString.split(":");
        int id = Integer.parseInt(details[0]);
        return new ClientUser(id, details[1]);
    }

}
