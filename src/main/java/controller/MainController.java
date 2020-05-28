package controller;

import backend.client.ChatClient;
import backend.client.PeerHandler;
import com.application.chatboxp2p.staticdata.Friend;
import ui.AddFriendUI;
import ui.MainUI;
import utils.PeerInfo;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainController implements Observer {
    private MainUI mainUI;
    private ChatClient chatClient;

    private PeerHandler currentPeer;

    private boolean first_time = true;

    public MainController(MainUI mainUI, ChatClient chatClient) {
        this.mainUI = mainUI;
        this.chatClient = chatClient;
    }

    public void initController() {
        for(PeerInfo p:this.chatClient.getClientInfo().friendList) {
            this.mainUI.getLf().addUser(new Friend(p.getName(), p.getStatus()));
        }
        this.mainUI.getList_user().addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                list_userMouseClicked(evt);
            }
        });

        this.mainUI.getSend_mess_but().addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendText();
            }
        });

        this.mainUI.getInput_text().addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if(evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER)
                {
                    sendText();
                    evt.consume();
                }
            }
        });

        this.mainUI.getLogout_but().addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (JOptionPane.showConfirmDialog(null, "Are you sure?", "Log out", JOptionPane.YES_NO_OPTION) == 0) {
//            loginUI.setVisible(true);
                    uiDispose();
                }
            }
        });

        this.mainUI.getAdd_user_but().addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_add_user_butActionPerformed
                AddFriendUI addFriendUI = new AddFriendUI();
                addFriendUI.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                addFriendUI.setVisible(true);
            }//GEN-LAST:event_add_user_butActionPerformed
        });
        this.mainUI.setVisible(true);
    }

    private void uiDispose() {
        this.chatClient.sendReq("disconnect");
        System.out.println("Feature");
        this.mainUI.dispose();
    }

    private void sendText() {
        String mess = this.mainUI.getInput_text().getText();
        if (!mess.equals(""))
        {
//            String[] indexString = text.split(" ");
//            if (indexString[0].equals("update") && indexString.length==3){
//                lf.updateStatus(indexString[1] , Integer.parseInt(indexString[2]));
//                list_user.updateUI();
//            }
            //Backend
            this.currentPeer.sendMessage(mess);

            //UI
            try {
                StyledDocument doc = this.currentPeer.getTextPane().getStyledDocument();
                Style style = this.currentPeer.getTextPane().addStyle("myStyle", null);
                JLabel label_me = new JLabel("Me:  ");
                label_me.setFont(new java.awt.Font("Times New Roman", 1, 16));
                label_me.setForeground(new java.awt.Color(160, 28, 28));
                StyleConstants.setComponent(style, label_me);
                doc.insertString(doc.getLength(), " ", style);

                JTextArea textArea = new JTextArea(mess);
                textArea.setLineWrap(true);
                textArea.setFont(new java.awt.Font("Times New Roman", 1, 14));

                StyleConstants.setComponent(style, textArea);
                doc.insertString(doc.getLength(), "\n", style);
            } catch (BadLocationException ex) {
                Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.mainUI.setInput_text("");
            System.out.println("[CLIENT] enter mess:" +mess);


        }
    }

    private void list_userMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_list_userMouseClicked
        JList list = (JList)evt.getSource();
        if (evt.getClickCount() == 2) {
            if (this.first_time){
                this.mainUI.getChat_box().setVisible(true);
                this.first_time = false;
            }

            int index = list.locationToIndex(evt.getPoint());
            System.out.println(index + "\n");
            String username = this.mainUI.getLf().getUserByIndex(index).getUser_name();
//            boolean isExist = false;
//            for(PeerHandler peer:this.chatClient.getPeerList()) {
//                if(username.equals(peer.getTargetClientName())) {
//                    isExist = true;
//                    break;
//                }
//            }
            if (this.chatClient.getPeerList().containsKey(username)) {
                System.out.println("Open chat box " + index);
                this.mainUI.getChat_section().removeAll();
                this.currentPeer = this.chatClient.getPeerList().get(username);
                this.mainUI.getChat_section().add(this.currentPeer.getTextPane());
//                this.mainUI.setCurrent_text_pane(textPane);
                this.mainUI.getUser_name_label().setText(username);
                this.mainUI.getChat_section().repaint();
//                    this.mainUI.getList_chat_section().put(index , temp);
                this.mainUI.getLf().updateStatus(index, 1);
                this.mainUI.getList_user().updateUI();
            }
            else {
                String req = "connectfriendto-" + this.chatClient.getClientInfo().getClientName() + "-" + username;
                long startTime = System.currentTimeMillis();
                this.chatClient.sendReq(req);
                String resMess = null;
                boolean connectSuccess = false;
                while((new Date()).getTime() - startTime < 1000*3) {
                    synchronized (this) {
                        resMess = this.chatClient.getResponseMessage();
                    }
                    if (resMess != null && resMess.equals("success-" + username)) {
                        connectSuccess = true;
                        break;
                    }
                }
                if (connectSuccess) {
                    this.chatClient.setResponseMessage(null);
                    System.out.println("Success connect friend " + username);
                    System.out.println("Create new Chat box");
//                    JTextPane temp = new JTextPane();
//                    temp.setSize(this.mainUI.getChat_section().getSize());
                    this.mainUI.getChat_section().removeAll();
                    this.currentPeer = this.chatClient.getPeerList().get(username);
                    this.mainUI.getChat_section().add(this.currentPeer.getTextPane());
//                    this.mainUI.setCurrent_text_pane(textPane);
                    this.mainUI.getUser_name_label().setText(username);
                    this.mainUI.getChat_section().repaint();
//                    this.mainUI.getList_chat_section().put(index , temp);
                    this.mainUI.getLf().updateStatus(index, 1);
                    this.mainUI.getList_user().updateUI();
                }
                else {
                    System.out.println("[CLIENT] failed to connect to " + username);
                }
//                do {
//                    synchronized (this) {
//                        resMess = this.chatClient.getResponseMessage();
//                    }
//                } while( resMess == null && (new Date()).getTime() - startTime < 1000*3);
//                if (resMess != null && resMess.equals("success-" + username)) {
//                    System.out.println("Success connect friend " + username);
//                    System.out.println("Create new Chat box");
////                    JTextPane temp = new JTextPane();
////                    temp.setSize(this.mainUI.getChat_section().getSize());
//                    this.mainUI.getChat_section().removeAll();
//                    this.currentPeer = this.chatClient.getPeerList().get(username);
//                    this.mainUI.getChat_section().add(this.currentPeer.getTextPane());
////                    this.mainUI.setCurrent_text_pane(textPane);
//                    this.mainUI.getUser_name_label().setText(username);
//                    this.mainUI.getChat_section().repaint();
////                    this.mainUI.getList_chat_section().put(index , temp);
//                    this.mainUI.getLf().updateStatus(index, 1);
//                    this.mainUI.getList_user().updateUI();
//                }
//                else{
//
//                }
            }
//            if (!isExist) {
//                String req = "connectfriendto-" + this.chatClient.getClientInfo().getClientName() + "-" + username;
//                this.chatClient.sendReq(req);
//                String resMess = null;
//                do {
//                    synchronized (this) {
//                        resMess = this.chatClient.getResponseMessage();
//                    }
//                } while( resMess == null);
//                if (resMess.equals("success")) {
//                    System.out.println("Success connect friend " + username);
//                    System.out.println("Create new Chat box");
////                    JTextPane temp = new JTextPane();
////                    temp.setSize(this.mainUI.getChat_section().getSize());
//                    this.mainUI.getChat_section().removeAll();
//                    JTextPane textPane = this.chatClient.getPeerList().get(username).getTextPane();
//                    this.mainUI.getChat_section().add(textPane);
//                    this.mainUI.setCurrent_text_pane(textPane);
//                    this.mainUI.getUser_name_label().setText(username);
//                    this.mainUI.getChat_section().repaint();
////                    this.mainUI.getList_chat_section().put(index , temp);
//                    this.mainUI.getLf().updateStatus(index, 1);
//                    this.mainUI.getList_user().updateUI();
//                }
//            } else{
//
//            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == this.chatClient) {

            String[] s = (String[])arg;
            System.out.println("Feature");
            if (s[0].equals("friendstatus")) {
                this.mainUI.updateFriendList(s[1], s[2]);
            } else if (s[0].equals("disconnect")) {
                if (s[1].equals(this.currentPeer.getTargetClientName())) {
                    this.mainUI.getChat_section().removeAll();
                    this.currentPeer = null;
                }
            }

        }
    }
}
