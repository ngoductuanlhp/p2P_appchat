package controller;

import backend.client.ChatClient;
import backend.server.ChatServer;
import backend.server.ClientHandler;
import ui.AddFriendUI;
import ui.MainUI;

import javax.print.MultiDocPrintService;
import javax.swing.*;

public class AddFriendController {
    AddFriendUI addFriendUI;
    ChatClient chatClient;

    public AddFriendController(AddFriendUI addFriendUI, ChatClient chatClient) {
        this.addFriendUI = addFriendUI;
        this.chatClient = chatClient;
    }

    public void initController() {
        this.addFriendUI.getAdd_but().addActionListener(e -> addFriend());

    }
    private void addFriend() {
        String friendName = this.addFriendUI.getUserInput();
        if (friendName.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Inavlid input!");
        } else {
            this.chatClient.sendReq("addfriend-" + friendName);
            System.out.println("addfriend-" + friendName);
        }
        String resMess = null;
        do {
            synchronized (this) {
                resMess = this.chatClient.getResponseMessage();
            }
        } while (resMess == null);
        if (resMess.equals("addfriend-success")) {
            this.addFriendUI.setVisible(false);
            MainUI mainUI = new MainUI();
            MainController mainController = new MainController(mainUI, this.chatClient);
            this.chatClient.addObserver(mainController);
            mainController.initController();
        }
        this.chatClient.setResponseMessage(null);
    }
}

 