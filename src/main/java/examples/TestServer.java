package examples;

import backend.server.ChatServer;

public class TestServer {
    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer(11111, 30);
        chatServer.start();
    }
}
