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

import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;



/**
 * The main class of the application.
 */
public class MachatApp {

    private static LinkedList<ChatWindow> chats;
    private static HashMap<Integer, Integer> contactsToWindowMap;
    private static MachatApp instance;
    private ContactsWindow contactsui;
    private ChatNetwork connection;
    private boolean connected;
    private static int nextChatId = 0;

    /**
     * At startup create and show the main frame of the application.
     */
    protected void startup() {
        // Get the native look and feel class name
        String nativeLF = UIManager.getSystemLookAndFeelClassName();
        
        // Install the look and feel
        try {
            UIManager.setLookAndFeel(nativeLF);
        } catch (InstantiationException e) {
        } catch (ClassNotFoundException e) {
        } catch (UnsupportedLookAndFeelException e) {
        } catch (IllegalAccessException e) {
        }
        contactsToWindowMap = new HashMap<Integer, Integer>();
        contactsui = new ContactsWindow();
        contactsui.setVisible(true);

        chats = new LinkedList<ChatWindow>();

        try {
            connection = new ChatNetwork();
            connected = true;
        } catch(NoConnectionException e) {
            contactsui.alert("Connection error", "No connection found. Could not start tchat.");
            connected = false;
        }

    }


    /**
     * A convenient static getter for the application instance.
     * @return the instance of TChatApp
     */
    public static MachatApp getApplication() {
        return instance;
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        instance = new MachatApp();
        instance.startup();
    }
    /**
     * Opens a new chat with a user.
     * @param userId The user to open a chat with.
     */
    public static void openNewChat(int userId) {
        ChatWindow newChat = new ChatWindow(userId);
        newChat.setVisible(true);
        chats.add(newChat);
        contactsToWindowMap.put(userId, nextChatId);
        nextChatId++;
    }
    /**
     * Temporary testing method until user systems are in place.
     * @param userId
     * @param msg
     */
    public void addOtherUserMessage(int userId, String msg) {
        getChatWindow(userId).addMessage("" + userId, msg);
    }
    /**
     * Adds a message from another user.
     * @param username The username of the person sending the message.
     * @param msg The message sent by the other user.
     */
    public void addOtherUserMessage(String username, String msg) {
        //ui.addMessage(username, msg);
    }
    /**
     * Gets a chat window for a certain contact.
     * @param contactId The contact ID to open a chat with
     * @return Either a new ChatWindow for that user, or a reference to an open one.
     */
    private static ChatWindow getChatWindow(int contactId) {
        if(contactsToWindowMap.get(contactId) == null) {
            openNewChat(contactId);
        }
        int chatWindowId = contactsToWindowMap.get(contactId);
        return chats.get(chatWindowId);
    }
    /**
     * Sends a message to another user.
     * @param message The message to send.
     * @param target The uid of the user to message.
     */
    public void sendMessage(String message, int target) {
        connection.sendMessage(message, target);
    }
    /**
     * Closes the connection, then the program.
     */
    public void close() {
        if(connected) {
            connection.quit();
        }
        System.exit(0);
    }
}