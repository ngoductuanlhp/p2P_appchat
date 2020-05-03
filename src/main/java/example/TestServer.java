package example;

import server.ChatServer;

public class TestServer {
    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer(11200, 30);
        chatServer.start();
    }
}
