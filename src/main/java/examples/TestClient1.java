package examples;

import backend.client.ChatClient;
import controller.LoginController;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import ui.LoginUI;

public class TestClient1 {
    public static void main(String[] args) throws UnknownHostException{
        LoginUI loginUI = new LoginUI();
        ChatClient chatClient = new ChatClient("192.168.0.111", 11111);
        LoginController loginController = new LoginController(loginUI, chatClient);
        loginController.initController();
    }
}
