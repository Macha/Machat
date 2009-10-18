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

import java.util.concurrent.LinkedBlockingQueue;
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
    private static HashMap<Integer, ConnectedClient> clientMap;
    /**
     * The main method for the application. Starts the server listening for connections.
     * @param args Unused
     */
    public static void main(String[] args) {
        clientMap = new HashMap<Integer, ConnectedClient>();
        nextId = 0;
        ServerSocket ss;
        try {
			ss = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("Unable to open server socket. Please check the port is not in use already.");
			ss = null; // This prevents a compiler error that ss is not initialised a few lines down.
					   // It does not seem to notice that the System.exit call prevents the whole while loop
					   // from running.
			System.exit(1);
		}
        Socket s;
        while(true) {
        	try {
        		s = ss.accept();
        		ConnectedClient cct = new ConnectedClient(s, nextId);
	            clientMap.put(nextId, cct);
	            System.out.println("Connect: " + nextId);
	            nextId++;
	            Thread t = new Thread(cct);
	            t.start();
        	} catch(IOException e) {
        		System.out.println("Failed to connect to a client");
        	}
        }
    }
    /**
     * Gets a client object from the client ID.
     * @param clientId The user ID of the client to message.
     * @return An object representing the connection (more specifically, a running Thread)
     */
    public static ConnectedClient getClient(int clientId) {
            return clientMap.get(clientId);
    }
    /**
     * Sends a message from one client to another.
     * @param targetId The ID of the person to recieve the message.
     * @param fromId The ID of the person sending the message.
     * @param message The contents of the message.
     */
    public static void sendMessage(int targetId, int fromId, String message) {
        ConnectedClient targetClient = getClient(targetId);
        System.out.println("Sending message: " + message + "\n\nfrom " + fromId +  " to " + targetId);
        targetClient.addOutCommand("/message:" + fromId + ":" + message + "\n");
    }
    /**
     * This method disconnects a client from the server.
     * @param client The connection id of the client.
     */
    public static void disconnect(int targetId) {
        ConnectedClient connectedClient = getClient(targetId);
        connectedClient.quit();
        clientMap.remove(targetId);
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
        } catch(InterruptedException e) {
            e.printStackTrace();
            // Do nothing.
        } catch(IOException e) {
        	System.out.println("Network access failure. Disconnecting: " + thisId);
        	quit();
        }
    }
    /**
     * This method causes the connection to close.
     */
    public synchronized void quit() {
        shouldExit = true;
    }
    /**
     * This adds a received command to be processed. It may not occur instantly.
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
        System.out.println("" + thisId + " recieved to send: " + command);
    }
    /**
     * This method returns the connection id of the thread.
     * @return
     */
    public int getId() {
        return thisId;
    }
}
class CommandProcessor implements Runnable {
    private LinkedBlockingQueue<String> inQueue;
    private LinkedBlockingQueue<String> outQueue;
    private OutputStreamWriter out;
    private int conId;
    public CommandProcessor(OutputStreamWriter out, ConnectedClient cct) {
        this.out = out;
        inQueue = new LinkedBlockingQueue<String>();
        outQueue = new LinkedBlockingQueue<String>();
        this.conId = cct.getId();
    }
    public void run() {
        String currentCommandIn;
        String currentCommandOut;
        while(true) {
            currentCommandIn = inQueue.poll();
            if(currentCommandIn != null) {
                String[] commandArr;
                try {
                    commandArr = CommandParser.parseRecievedCommand(currentCommandIn);
                } catch(IOException e) {
                	System.out.println("Invalid command recieved: " + currentCommandIn);
                	commandArr = new String[1];
                	commandArr[0] = null;
                }
                this.handleParsedCommand(commandArr);
            }
            try {
                currentCommandOut = outQueue.poll();
            } catch(NullPointerException e) {
            	currentCommandOut = null;
            }
            if(currentCommandOut != null) {
                try {
                    out.write(currentCommandOut + "\n");
                    System.out.println(currentCommandOut + "sent");
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
    /**
     * This adds a received command to be processed at some future point.
     * @param command The command to be processed.
     */
    public synchronized void addInCommand(String command) {
        if(command != null) {
            try {
				inQueue.put(command);
			} catch (InterruptedException e) {
			}
            System.out.println("Recieved In: " + command);
        }
    }
    /**
     * This adds an outgoing command to be sent to the client.
     * @param command The command to be sent
     */
    public synchronized void addOutCommand(String command) {
    	if(command != null) {
    		try {
    			outQueue.put(command);
    		} catch(InterruptedException e) {
    		}
            System.out.println("Ready to send: " + command);
        } else {
        	System.out.println("Null command recieved");
        }
    }
    public void handleParsedCommand(String[] commandArr) {
		if(commandArr[0].equalsIgnoreCase("message")) {
            int target = Integer.parseInt(commandArr[1]);
            String message = commandArr[2];
            System.out.println("Message sending to: " + target);
            MachatServer.sendMessage(target, this.conId, message);
        } else if(commandArr[0].equalsIgnoreCase("quit")) {
            // Tell the server to disconnect us.
            MachatServer.disconnect(conId);
        } else if(commandArr[0].equalsIgnoreCase("confirmconnect")) {
        	
        } else {
            try {
                out.write("Unknown: " + commandArr[0] + "\n");
            } catch (IOException e) {
                System.out.println("Failed output warning of unknown command.");
            }
        }
    }
}