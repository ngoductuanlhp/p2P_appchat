package examples;

import backend.client.ChatClient;
import controller.LoginController;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import ui.LoginUI;

public class TestClient {
    public static void main(String[] args) throws UnknownHostException{
        LoginUI loginUI = new LoginUI();
<<<<<<< HEAD
        ChatClient chatClient = new ChatClient("192.168.0.100", 11111);
=======
        ChatClient chatClient = new ChatClient("192.168.0.111", 11111);
>>>>>>> e75195f0d2f81f6c5e6f3d4e5e617737c7362544
        LoginController loginController = new LoginController(loginUI, chatClient);
        loginController.initController();
    }
}
