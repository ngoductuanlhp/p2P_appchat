package backend.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;

import org.apache.commons.lang3.StringUtils;
import utils.ClientInfo;

public class ClientHandler implements Runnable{
    private final ChatServer server;
    private final Socket client;

    private DataInputStream reader;
    private DataOutputStream writer;

    public boolean logged = false;

    private ClientInfo clientInfo = null;

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
                            this.handleLogin(segments);
                            break;
                        case "logout":
                            this.handleLogout();
                            break;
                        case "addfriend":
                            this.handleAddFriend(segments[0], segments[1]);
                            break;
                        case "connectfriendto":
                            this.handleConnectFriend(segments[1], segments[2]);
                            break;
                        case "removefriend":
                            this.handleRemoveFriend(segments[0], segments[1]);
                            break;
                        case "acceptconnectfriend":
                            this.handleConnectFriendFrom(segments[1], segments[2], segments[3]);
                            break;
//                        case "notifyonline":
//                            this.handleNotifyLogIn();
//                            break;
                        case "disconnect":
                            this.handleDisconnect();
                            break;
                        default:
                            System.out.println("Unknown " + type);
                    }
                }
            }
        } catch (IOException e) {
//            e.printStackTrace();
            System.out.println("[SERVER] Client disconnected");
            try {
                handleLogout();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    private void handleDisconnect() throws IOException {
        System.out.println("cc");
        server.markOffline(clientInfo.getClientName());
        this.notifyOffline();
    }

    private void notifyOnline() throws IOException {
//        LinkedList<String> listOfOnlineFriend = server.findOnlineFriend(clientInfo.getClientName());
        for (String friend : server.getFriendList(clientInfo.getClientName())) {
            ClientHandler c = server.getClientHandler(friend);
            if (c != null) {
//                System.out.println("Notify");
                c.sendOnlineNotify(clientInfo.getClientName());
            }
        }
    }

    public void sendOnlineNotify(String target) throws IOException {
        this.writer.writeUTF("notifyonline" + "-" + target);
    }

    private void notifyOffline() throws IOException {
        server.markOffline(clientInfo.getClientName());
//        LinkedList<String> listOfOnlineFriend = server.findOnlineFriend(clientInfo.getClientName());
        for (String friend : server.getFriendList(clientInfo.getClientName())) {
            ClientHandler c = server.getClientHandler(friend);
            if (c != null) {
                c.sendOfflineNotify(clientInfo.getClientName());
            }
        }
        this.server.removeClientHandler(this);
    }

    public void sendOfflineNotify(String target) throws IOException {
        this.writer.writeUTF("notifyoffline" + "-" + target);
    }

    private void handleSignup(String[] segments) throws IOException {
        System.out.println(String.format("[SERVER] Sign-up with username %s, password %s", segments[1], segments[2]));
        if (!server.findUsername(segments[1])) {
            server.createAccount(segments[1], segments[2]);
            this.clientInfo = new ClientInfo(segments[1]);
            server.markOnline(clientInfo.getClientName());
            sendSuccessRes(segments[0], segments[1]);
        } else {
            sendFailedRes(segments[0]);
        }
    }

    private void handleLogin(String[] segments) throws IOException {
        if (server.checkPassword(segments[1], segments[2])) {
//            clientInfo.setClientName(segments[1]);
            this.clientInfo = new ClientInfo(segments[1]);
            server.markOnline(clientInfo.getClientName());
            sendSuccessRes(segments[0], segments[1]);
            notifyOnline();
        } else {
            sendFailedRes(segments[0]);
        };
    }

    private void handleLogout() throws IOException {
        server.markOffline(clientInfo.getClientName());
        notifyOffline();
    }

    private void handleAddFriend(String req, String friendName) throws IOException {
        if (server.findUsername(friendName)) {
            server.removeFriend(clientInfo.getClientName(), friendName);
            notifyOnline();
            sendSuccessRes(req, friendName);
        } else {
            sendFailedRes(req);
        }
    }

    private void handleConnectFriend(String nameFrom, String nameTo) throws IOException {
        ClientHandler c = this.server.getClientHandler(nameTo);
        if (c != null) {
            c.sendRequestConnectFriend(nameFrom, nameTo);
        } else {
            sendFailedRes("connecfriendto");
        }
    }

    private void handleConnectFriendFrom(String nameFrom, String nameTo ,String port) throws IOException {
        ClientHandler c = server.getClientHandler(nameFrom);
//        System.out.println(c);
        if (c != null) {
            String ip = this.client.getInetAddress().toString().substring(1);
            c.sendReponseConnectFriend(nameFrom, nameTo, ip, port);
        }
    }

    private void handleRemoveFriend(String req, String friendName) throws IOException {
        if (server.findUsername(friendName)) {
            server.addFriend(clientInfo.getClientName(), friendName);
            notifyOnline();
            sendSuccessRes(req, friendName);
        } else {
            sendFailedRes(req);
        }
    }

    private void sendSuccessRes(String req, String username) throws IOException {
        switch (req) {
            case "signup":
                writer.writeUTF("signup-" + "success-" + username + '-' + "0");
                break;
            case "login":
                LinkedList<String> friend = server.getFriendList(clientInfo.getClientName());
                String res = "login-" + "success-" + username + "-" + String.valueOf(friend.size());
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(res);
                for (String n:friend) {
                    stringBuilder.append("-").append(n);
                    stringBuilder.append("-").append(server.getClientStatus(n));
                }
                writer.writeUTF(stringBuilder.toString());
                break;
            case "logout":
                writer.writeUTF("logout-" + "success");
                break;
            case "addfriend":
                writer.writeUTF("addfriend-" + "success");
                break;
        }
    }

    private void sendFailedRes(String req) throws IOException {
        switch (req) {
            case "signup":
                writer.writeUTF("signup-" + "failed");
                break;
            case "login":
                writer.writeUTF("login-" + "failed");
                break;
            case "addfriend":
                writer.writeUTF("addfriend-" + "failed");
                break;
            case "connectfriendto":
                writer.writeUTF("connectfriendto-" + "failed");
                break;
        }
    }

    public void sendRequestConnectFriend(String nameFrom, String nameTo) throws IOException {
        this.writer.writeUTF("connectfriendto-" + nameFrom + "-" + nameTo);
    }

    public void sendReponseConnectFriend(String nameFrom, String nameTo, String ip, String port) throws IOException{
//        System.out.println(client.getInetAddress().toString().substring(1));
        this.writer.writeUTF("acceptconnectfriend-" + nameFrom + "-" + nameTo + "-" + ip + "-" +port);
    }

    public ClientInfo getClientInfo() {
        return clientInfo;
    }

}
