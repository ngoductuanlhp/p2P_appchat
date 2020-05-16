package examples;

import backend.client.ChatClient;
import controller.LoginController;
import ui.LoginUI;

public class TestClient {
    public static void main(String[] args) {
        LoginUI loginUI = new LoginUI();
        ChatClient chatClient = new ChatClient("192.168.0.134", 11111);
        LoginController loginController = new LoginController(loginUI, chatClient);
        loginController.initController();
    }
}
