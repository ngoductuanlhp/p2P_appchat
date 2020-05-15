package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import org.apache.commons.lang3.StringUtils;
import utils.ClientInfo;

public class ClientHandler implements Runnable{
    private final ChatServer server;
    private final Socket client;

    private DataInputStream reader;
    private DataOutputStream writer;

    private ClientInfo clientInfo;

    public ClientHandler(ChatServer server, Socket client) {
        this.server = server;
        this.client = client;
    }

    @Override
    public void run() {
        try {
            this.reader = new DataInputStream(client.getInputStream());
            this.writer = new DataOutputStream(client.getOutputStream());

            String message      = null;
            String[] segments   = null;
            while(true) {
                message = reader.readUTF();
                System.out.print(String.format("[SERVER] Received request: %s\n", message));
                segments = StringUtils.split(message,'-');
                if (segments != null && segments.length > 0) {
                    String type = segments[0];
                    switch (type) {
                        case "signup":
                            this.handleSignup(segments);
                            break;
                        case "login":
                            this.handleLogin();
                            break;
                        case "logout":
                            this.handleLogout();
                            break;
                        case "addfriend":
                            this.handleAddfriend();
                            break;
                        case "connectfriend":
                            this.handleConnectfriend();
                            break;
                        default:
                            System.out.println("Unknown " + type);
                    }
                }
            }
        } catch (IOException e) {
//            e.printStackTrace();
            System.out.println("[SERVER] Client disconnected");
        }
    }

    private void handleSignup(String[] segments) {
        System.out.println(String.format("[SERVER] Sign-up with username %s, password %s", segments[1], segments[2]));
    }

    private void handleLogin() {

    }

    private void handleLogout() {

    }

    private void handleAddfriend() {

    }

    private void handleConnectfriend() {

    }

}
