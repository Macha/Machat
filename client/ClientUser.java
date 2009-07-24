/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
