package controller;

import backend.client.ChatClient;
import backend.client.PeerHandler;
import com.application.chatboxp2p.staticdata.Friend;
import ui.MainUI;
import utils.PeerInfo;

import javax.swing.*;

public class MainController {
    private MainUI mainUI;
    private ChatClient chatClient;

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
        this.mainUI.setVisible(true);
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
            String name = this.mainUI.getLf().getUserByIndex(index).getUser_name();
            boolean isExist = false;
            for(PeerHandler peer:this.chatClient.getPeerList()) {
                if(name.equals(peer.getTargetClientName())) {
                    isExist = true;
                    break;
                }
            }
            if (!isExist) {
                String req = "connectfriendto-" + this.chatClient.getClientInfo().getClientName() + "-" + name;
                this.chatClient.sendReq(req);
                String resMess = null;
                do {
                    synchronized (this) {
                        resMess = this.chatClient.getResponseMessage();
                    }
                } while( resMess == null);
                if (resMess.equals("success")) {
                    System.out.println("Success connect friend " + name);
                }


                System.out.println("Create new Chat box");
                JTextPane temp = new JTextPane();
                temp.setSize(this.mainUI.getChat_section().getSize());
                this.mainUI.getChat_section().removeAll();
                this.mainUI.getChat_section().add(temp);
                this.mainUI.setCurrent_text_pane(temp);
                this.mainUI.getUser_name_label().setText(this.mainUI.getLf().getUserByIndex(index).getName());
                this.mainUI.getChat_section().repaint();
                this.mainUI.getList_chat_section().put(index , temp);
                this.mainUI.getLf().updateStatus(index, 1);
                this.mainUI.getList_user().updateUI();
            } else{
                System.out.println("Open chat box " + index);
                this.mainUI.getChat_section().removeAll();
                this.mainUI.setCurrent_text_pane(this.mainUI.getList_chat_section().get(index));
                this.mainUI.getChat_section().add(this.mainUI.getCurrent_text_pane());
                this.mainUI.getUser_name_label().setText(this.mainUI.getLf().getUserByIndex(index).getName());
                this.mainUI.getChat_section().repaint();
                this.mainUI.getLf().updateStatus(index, 1);
                this.mainUI.getList_user().updateUI();
            }
        }
    }
}
