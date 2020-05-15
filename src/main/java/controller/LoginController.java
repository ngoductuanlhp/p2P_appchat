package controller;

import backend.client.ChatClient;
import ui.LoginUI;
import ui.MainUI;

import javax.swing.*;

public class LoginController {
    LoginUI loginUI;
    ChatClient chatClient;

    public LoginController(LoginUI loginUI, ChatClient chatClient) {
        this.loginUI = loginUI;
        this.chatClient = chatClient;
    }

    public void initController() {
        this.chatClient.start();
        this.loginUI.getLogin_but().addActionListener(e->login());
        this.loginUI.setVisible(true);
    }

    private void login() {
        String username = this.loginUI.getUser_input();
        String password = this.loginUI.getPass_input();
        if (username.isEmpty() || password.isEmpty())
        {
            JOptionPane.showMessageDialog(null, "Inavlid input!");
        }
        else {
            this.chatClient.sendReq("login-" + username + "-"+ password);
        }
        String resMess = null;
        do {
            synchronized (this) {
                resMess = this.chatClient.getResponseMessage();
            }
        } while (resMess == null);
        if (resMess.equals("success")) {
            this.loginUI.setVisible(false);
            MainUI mainUI = new MainUI();
            MainController mainController = new MainController(mainUI, this.chatClient);
            mainController.initController();
        }

    }
}
