/*
 * TChatApp.java
 */

/**
 * This package contains all code specific to the client.
 */
package machat.client;

import java.util.HashMap;
import java.util.LinkedList;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class MachatApp extends SingleFrameApplication {

    private static LinkedList<ChatWindow> chats;
    private static HashMap<Integer, Integer> contactsToWindowMap;
    private ContactsWindow contactsui;
    private ChatNetwork connection;
    private boolean connected;
    private static int nextChatId = 0;

    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
        contactsToWindowMap = new HashMap();
        contactsui = new ContactsWindow();
        show(contactsui);

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
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of TChatApp
     */
    public static MachatApp getApplication() {
        return Application.getInstance(MachatApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(MachatApp.class, args);
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