package example;

import client.ChatClient;
import server.ChatServer;

public class TestClient {
    public static void main(String[] args) {
        ChatClient chatClient = new ChatClient("localhost", 11200);
        chatClient.start();
    }
}
