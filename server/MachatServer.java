/**
 * This package contains all server side code.
 */
package machat.server;

import java.util.logging.Level;
import java.util.logging.Logger;
import machat.lib.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.util.*;

import machat.lib.CommandParser;
import machat.lib.MachatUser;


/**
 * The main class for the server.
 * @author Macha <macha.hack@gmail.com>
 */
public class MachatServer {
    private static int port = 1234;
    private static int nextId;
    private static LinkedList<ConnectedClient> clients;
    private static HashMap clientToConnectionMap;
    /**
     * The main method for the application. Starts the server listening for connections.
     * @param args Unused
     */
    public static void main(String[] args) {
        clientToConnectionMap = new HashMap();
        clients = new LinkedList<ConnectedClient>();
        nextId = 0;
        try {
            ServerSocket ss;
            ss = new ServerSocket(port);
            Socket s;
            while(true) {
                s = ss.accept();
                ConnectedClient cct = new ConnectedClient(s, nextId);
                clients.add(nextId, cct);
                registerConnection(nextId, nextId);
                System.out.println("Connect: " + nextId);
                nextId++;
                Thread t = new Thread(cct);
                t.start();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Sets a connection up so that messages can be recieved.
     */
    public static void registerConnection(int userId, int connectionId) {
        clientToConnectionMap.put(userId, connectionId);
    }
    /**
     * Gets the ID of a connected user.
     * @param clientId
     * @return
     */
    public static int getClientConnectionId(int clientId) {
        return (Integer)clientToConnectionMap.get(clientId);
    }
    /**
     * Gets a client object from the client ID.
     * @param clientId The user ID of the client to message.
     * @return An object representing the connection (more specifically, a running Thread)
     */
    public static ConnectedClient getClient(int clientId) {
        return clients.get(getClientConnectionId(clientId));
    }
    /**
     * Sends a message from one client to another.
     * @param targetId The ID of the person to recieve the message.
     * @param fromId The ID of the person sending the message.
     * @param message The contents of the message.
     */
    public static void sendMessage(int targetId, int fromId, String message) {
        ConnectedClient targetClient = getClient(targetId);
        targetClient.addOutCommand("/message:" + fromId + ":" + message);
    }
    /**
     * This method disconnects a client from the server.
     * @param client The connection id of the client.
     */
    public static void disconnect(int targetId) {
        ConnectedClient connectedClient = getClient(targetId);
        connectedClient.quit();
        clients.set(targetId, null);
        System.out.println("Disconnect: " + targetId);
    }

}
/**
 * This Thread represents a connection.
 * @author Macha <macha.hack@gmail.com>
 */
class ConnectedClient implements Runnable {
    private boolean shouldExit;
    private Socket s;
    private Scanner in;
    private int thisId;
    private OutputStreamWriter out;
    private CommandProcessor commandProcessor;

    /**
     * Creates a new connection.
     * @param s The socket to use.
     */
    public ConnectedClient(Socket s, int thisId) throws IOException {
        this.s = s;
        this.thisId = thisId;
        this.shouldExit = false;
        in = new Scanner(s.getInputStream(), "UTF-8");
        out = new OutputStreamWriter(new BufferedOutputStream(s.getOutputStream()), "UTF-8");

        commandProcessor = new CommandProcessor(out, this);
        Thread t = new Thread(commandProcessor);
        t.start();
    }
    public void run() {
        String contact;
        contact = s.getInetAddress().toString();
        System.out.println("Connected to " + contact);
        try {
            out.write("/connected" + "\n");
            out.flush();
            String command;
            while(true) {
                if(shouldExit) {
                    s.close();
                    break;
                }
                if(in.hasNextLine()) {
                    command = in.nextLine();
                    commandProcessor.addInCommand(command);
                }
                Thread.sleep(100);
             }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * This method causes the connection to close.
     */
    public void quit() {
        shouldExit = true;
    }
    /**
     * This adds a  recieved command to be processed. It may not occur instantly.
     * Should only be called by this class, and commandProcessor.
     * @param command The command to be processed.
     */
    public void addInCommand(String command) {
        commandProcessor.addInCommand(command);
    }
    /**
     * This adds a outgoing command to be sent. It may not occur instantly.
     * @param command The command to be sent.
     */
    public void addOutCommand(String command) {
        commandProcessor.addOutCommand(command);
    }
    /**
     * This method returns the connection id of the thread.
     * @return
     */
    public int getConnectionId() {
        return thisId;
    }
}
class CommandProcessor implements Runnable {
    private LinkedList<String> inQueue;
    private LinkedList<String> outQueue;
    private OutputStreamWriter out;
    private ConnectedClient cct;
    private int conId;
    public CommandProcessor(OutputStreamWriter out, ConnectedClient cct) {
        this.cct = cct;
        this.out = out;
        inQueue = new LinkedList<String>();
        this.conId = cct.getConnectionId();
    }
    public void run() {
        String currentCommandIn;
        String currentCommandOut;
        while(true) {
            try {
                currentCommandIn = inQueue.poll();
                if(currentCommandIn != null) {
                    System.out.println(currentCommandIn);
                    String[] commandArr = CommandParser.parseRecievedCommand(currentCommandIn);
                    if(commandArr[0].equalsIgnoreCase("message")) {
                        int target = Integer.parseInt(commandArr[1]);
                        String message = commandArr[2];
                        try {
                            out.flush();
                        } catch (IOException ex) {
                            Logger.getLogger(CommandProcessor.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        MachatServer.sendMessage(target, conId, message);
                    } else if(commandArr[0].equalsIgnoreCase("quit")) {
                        // Tell the server to disconnect us.
                        MachatServer.disconnect(cct.getConnectionId());
                        break;
                    } else {
                        try {
                            out.write("Unknown: " + currentCommandIn + "\n");
                        } catch (IOException ex) {
                            Logger.getLogger(CommandProcessor.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                currentCommandOut = outQueue.poll();
                if(currentCommandOut != null) {
                    try {
                        out.write(currentCommandOut + "\n");
                        System.out.println(currentCommandOut + "sent");
                        out.flush();
                    } catch (IOException ex) {
                        Logger.getLogger(CommandProcessor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch(Exception e) {
                // e.dontPrintStackTrace()
            }
        }
    }
    /**
     * This adds a recieved command to be processed at some future point.
     * @param command The command to be processed.
     */
    public synchronized void addInCommand(String command) {
        if(command != null) {
            inQueue.push(command);
            System.out.println("Recieved In: " + command);
        }
    }
    /**
     * This adds an outgoing command to be sent to the client.
     * @param command The command to be sent
     */
    public synchronized void addOutCommand(String command) {
        if(command != null) {
                outQueue.push(command);
                System.out.println("Ready to send: " + command);
        }
    }
}