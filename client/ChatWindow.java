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

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JMenuBar;



/**
 * This class is the window for a conversation
 * @author Macha <macha.hack@gmail.com>
 */
public class ChatWindow extends javax.swing.JFrame {

	
    /**
	 * 
	 */
	private static final long serialVersionUID = -1884248914631495966L;
    private MachatApp app;

    private JMenuItem exitMenuItem;
    private JMenuBar menuBar;
    private JTabbedPane tabs;
    /** 
     * Creates new form ChatWindow
     */
    public ChatWindow() {
        initComponents();
        this.app = MachatApp.getApplication();
        this.setTitle("Machat");
    }
    
    public synchronized ChatPanel openNewChat(int userId) {
    	ChatPanel newPanel = new ChatPanel(userId);
    	tabs.addTab(new Integer(userId).toString(), newPanel);
    	return newPanel;
    }

    /** 
	 * This method sets up the GUI for use (was originally created by Netbeans editor)
     */
    private void initComponents() {

        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        setName("Form"); 

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
        
        tabs = new JTabbedPane();
        this.add(tabs);

        pack();
    }


    private void helpMenuActionPerformed(java.awt.event.ActionEvent evt) {
    	// TODO

    }

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        MachatApp.getApplication().close();
    }

}
