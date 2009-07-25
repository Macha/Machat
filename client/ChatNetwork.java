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

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import java.util.Scanner;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import machat.lib.CommandParser;


/**
 * Represents the network connection for the client.
 * @author Macha <macha.hack@gmail.com>
 */
public class ChatNetwork {

    private static String host = "localhost";
    private static int port = 1234;
    private int id;
    private LinkedList<String> commandQueue;

    /**
     * Constructor that creates the command queue and begins the network thread.
     */
    public ChatNetwork() throws NoConnectionException {
        this.commandQueue = new LinkedList<String>();
        Socket socket = getSocket(port);
        Thread netInThread = new Thread(new NetInThread(socket, this));
        Thread netOutThread = new Thread(new NetOutThread(socket, this));
        
        netInThread.start();
        netOutThread.start();
    }
    /**
     * This method gets the connection id of this client.
     * @return The connection id.
     */
    public int getConnectionId() {
        return id;
    }
    /**
     * Should only be called by NetInThread
     * Sets the connection id (using data retrieved from the server)
     * @param id The Connection id (as specified by the server)
     */
    public void setConnectionId(int id) {
        this.id = id;
    }
    /**
     * Helper method to get a socket connected
     * @param port The port to connect to.
     * @return A socket connected to that port.
     */
    private static Socket getSocket(int port) throws NoConnectionException {
        Socket s;
        InetAddress ip;
        try {
            ip = InetAddress.getByName(host);
            s = new Socket(ip, port);
            return s;
        } catch (UnknownHostException e) {
            throw new NoConnectionException();
        } catch (IOException e) {
            throw new NoConnectionException();
        }
    }
    /**
     * Sends a message to a user.
     * @param message The message to send Should not contain newlines.
     * @param uid The user to message.
     */
    public void sendMessage(String message, int uid) {
        String command = "/message:" + uid + ":" + message;
        commandQueue.addLast(command);
        System.out.println(commandQueue);
    }
    /**
     * Disconnects from the network.
     */
    public void quit() {
        commandQueue.addLast("/quit");
        try {
            // Give it time to give the other thread a chance to send /quit .
            // If it doesn't manage, the server will figure it out, but it
            // doesn't hurt to let it send.
            Thread.sleep(50);
        } catch (InterruptedException ex) {
            // If it's interrupted, we're exiting anyway. Leave the server figure
            // it out
        }
    }
    /**
     * Gets the first item in the queue to be processed.
     * @return A command that is first in the queue.
     * @throws java.util.NoSuchElementException
     */
    public synchronized String getQueueItem() throws NoSuchElementException {
        return commandQueue.poll();
    }
}
/**
 * This thread handles all outgoing network code.
 * @author Macha <macha.hack@gmail.com>
 */
class NetOutThread implements Runnable {
    private MachatApp app;
    private Socket s;
    private OutputStreamWriter out;
    private ChatNetwork network;
    public NetOutThread(Socket s, ChatNetwork network) {
        this.app = MachatApp.getApplication();
        this.s = s;
        this.network = network;

        try {
            out = new OutputStreamWriter(new BufferedOutputStream(s.getOutputStream()), "UTF-8");
            out.write("/confirmconnect" + "\n");
            System.out.println("NetOutThread started");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void run() {
        String currentCommandOut = "";
        while(true) {
            try {
                currentCommandOut = network.getQueueItem();
                if(currentCommandOut != null) {
                    System.out.println(currentCommandOut + " sent");
                    out.write(currentCommandOut + "\n");
                    out.flush();
                }
            } catch(NoSuchElementException e) {
                System.out.println("NoSuchElementException");
            } catch(Exception e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                System.out.println("InterruptedException");
            }
        }
    }
}
/**
 * This thread handles all incoming network code.
 * @author Macha <macha.hack@gmail.com>
 */
class NetInThread implements Runnable {
    private MachatApp app;
    private Socket s;
    private Scanner in;
    private ChatNetwork network;
    private int id;
    
    public NetInThread(Socket s, ChatNetwork network) {
        this.app = MachatApp.getApplication();
        this.s = s;
        this.network = network;

        try {
            in = new Scanner(s.getInputStream(), "UTF-8");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        String currentCommandIn = "";
        String[] command;
        while(true) {
            currentCommandIn = in.nextLine();
            try {
                if(currentCommandIn != null) {
                    command = CommandParser.parseRecievedCommand(currentCommandIn);
                    if(command[0].equals("connected")) {
                    } else if(command[0].equals("message")) {
                        int fromId = new Integer(command[1]);
                        MachatApp.getApplication().addOtherUserMessage(fromId, command[2]);
                    }
                    System.out.println("Recieved: " + currentCommandIn);
                }
            } catch(NullPointerException e) {
                //TODO find cause and handle properly.
            }
        }
    }
}