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


import javax.swing.JDialog;


/**
 * This class is the window for a conversation
 * @author Macha <macha.hack@gmail.com>
 */
public class ChatWindow extends javax.swing.JFrame {

	
    private final static String newline = "\n";
    private MachatApp app;
    private JDialog aboutBox;
    private int contactId;

    /** 
     * Creates new form ChatWindow
     */
    public ChatWindow(int userId) {
        initComponents();
        contactId = userId;
        this.app = MachatApp.getApplication();
        this.setTitle("Machat - Chatting with " + userId);
    }
    /**
     * Adds a message to the UI
     * @param username The username of the message sender (or You)
     * @param msg The message to add.
     */
    public void addMessage(String username, String msg) {
        logTextArea.append(username + " said: " + msg + newline);
    }

    /**
     * Sends a message to the user that you are currently chatting with.
     */
    public void sendMessage() {
        String currentMessage = messageTextArea.getText();
        addMessage("You", currentMessage);
        app.sendMessage(currentMessage, contactId);
        messageTextArea.setText("");
    }

    /** 
	 * This method sets up the GUI for use (was originally created by Netbeans editor)
     */
    private void initComponents() {

        messageScrollPane = new javax.swing.JScrollPane();
        messageTextArea = new javax.swing.JTextArea();
        sendButton = new javax.swing.JButton();
        logScrollPane = new javax.swing.JScrollPane();
        logTextArea = new javax.swing.JTextArea();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        setName("Form"); 

        messageScrollPane.setName("messageScrollPane"); 

        messageTextArea.setColumns(20);
        messageTextArea.setRows(5);
        messageTextArea.setName("messageTextArea"); 
        messageTextArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                messageTextAreaKeyPressed(evt);
            }
        });
        messageScrollPane.setViewportView(messageTextArea);

        sendButton.setText("Send"); 
        sendButton.setName("sendButton"); 
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendButtonActionPerformed(evt);
            }
        });

        logScrollPane.setName("logScrollPane"); 

        logTextArea.setColumns(20);
        logTextArea.setEditable(false);
        logTextArea.setRows(5);
        logTextArea.setName("logTextArea"); 
        logScrollPane.setViewportView(logTextArea);

        menuBar.setName("menuBar"); 

        fileMenu.setMnemonic('F');
        fileMenu.setText("File"); 
        fileMenu.setName("fileMenu"); 

        exitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        exitMenuItem.setText("Exit"); 
        exitMenuItem.setName("exitMenuItem"); 
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setMnemonic('H');
        helpMenu.setText("Help"); 
        helpMenu.setName("helpMenu"); 
        helpMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpMenuActionPerformed(evt);
            }
        });



        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(logScrollPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(messageScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sendButton, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(logScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sendButton, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                    .addComponent(messageScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
                .addGap(19, 19, 19))
        );

        pack();
    }

    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {
        sendMessage();
    }

    private void helpMenuActionPerformed(java.awt.event.ActionEvent evt) {

    }

    private void messageTextAreaKeyPressed(java.awt.event.KeyEvent evt) {
        if(evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            sendMessage();
            evt.consume();
        }
    }

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        MachatApp.getApplication().close();
    }

    // Variables declaration
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JScrollPane logScrollPane;
    private javax.swing.JTextArea logTextArea;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JScrollPane messageScrollPane;
    private javax.swing.JTextArea messageTextArea;
    private javax.swing.JButton sendButton;
    // End of variables declaration

}
