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

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 * This windows is for the contacts listing that shows  when the program starts up
 * @author Macha <macha.hack@gmail.com>
 */
public class ContactsWindow extends javax.swing.JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 8431907581067825120L;
	private DefaultListModel listData;
    private int lastClicked;

    /** 
     * Creates new form ContactsWindow
     */
    public ContactsWindow() {
        initComponents();
        listData = new DefaultListModel();
        listData.addElement("Test Contact");
        contactList.setModel(listData);
        setTitle("Contacts - Machat");

    }
    /**
     * Helper method to display an alert.
     * @param title The title for the window.
     * @param message The message to alert with.
     */
    public void alert(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.WARNING_MESSAGE);
    }

    /** 
	 * This method sets up the GUI for use (was originally created by Netbeans editor)
     */
    @SuppressWarnings("serial")
	private void initComponents() {

        listScroller = new javax.swing.JScrollPane();
        contactList = new javax.swing.JList();
        mainMenuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newChatMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        exitMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setName("Form"); // NOI18N

        listScroller.setName("listScroller"); // NOI18N

        contactList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Default Contact", "If you can read this, something has gone wrong" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        contactList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        contactList.setName("contactList"); // NOI18N
        contactList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                contactListValueChanged(evt);
            }
        });
        listScroller.setViewportView(contactList);

        mainMenuBar.setName("mainMenuBar"); // NOI18N

        fileMenu.setText("File"); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        newChatMenuItem.setText("Open New Chat"); // NOI18N
        newChatMenuItem.setName("newChatMenuItem"); // NOI18N
        newChatMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newChatMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(newChatMenuItem);

        jSeparator1.setName("jSeparator1"); // NOI18N
        fileMenu.add(jSeparator1);

        exitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        exitMenuItem.setText("Exit"); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        mainMenuBar.add(fileMenu);

        setJMenuBar(mainMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(listScroller, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(listScroller, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
    	MachatApp.getApplication().close();
    }

    private void contactListValueChanged(javax.swing.event.ListSelectionEvent evt) {
        if (evt.getValueIsAdjusting() == false) {

            if (contactList.getSelectedIndex() == -1) {
                // No selection. Do nothing.
            } else {
                if(lastClicked != contactList.getSelectedIndex()) {
                     lastClicked = contactList.getSelectedIndex();
                } else {
                    // For the time being 1 is a placeholder. Switch to getting
                    // the contact id when that is implemented.
                    MachatApp.getApplication().openNewChat(1);
                }
            }
        }

    }

    private void newChatMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        int contactId = 0;
        try {
            contactId = new Integer(JOptionPane.showInputDialog("Please enter the ID of the user you wish to message."));
        } catch(NumberFormatException e) {
            // Test code. Final version will use usernames instead of ids.
        }
        MachatApp.getApplication().openNewChat(contactId);
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ContactsWindow().setVisible(true);
            }
        });
    }

    // Variables declaration 
    private javax.swing.JList contactList;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JScrollPane listScroller;
    private javax.swing.JMenuBar mainMenuBar;
    private javax.swing.JMenuItem newChatMenuItem;
    // End of variables declaration

}
