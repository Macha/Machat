/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
