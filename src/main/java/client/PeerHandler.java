package client;

import utils.ClientInfo;

import java.io.*;
import java.net.Socket;

public class PeerHandler implements Runnable{
//    ClientInfo hostClient;
//    ClientInfo targetClient;
    private String targetClientName;
    ChatClient client;

    private Socket socket;

    private InputStream is;
    private OutputStream os;

    private MessageSender messageSender;
    private MessageReceiver messageReceiver;
    private Thread messageSenderThread;
    private Thread messageReceiverThread;

    public PeerHandler(Socket socket, String targetClientName) {
        this.socket = socket;
        this.targetClientName = targetClientName;
    }

    public ChatClient getClient() {
        return this.client;
    }

    public String getTargetClientName() {
        return this.targetClientName;
    }

    @Override
    public void run() {
        try {
            this.is = this.socket.getInputStream();
            this.os = this.socket.getOutputStream();
            this.messageSender = new MessageSender(new DataOutputStream(this.os), this.getClient().getClientInfo());
            this.messageReceiverThread = new Thread(this.messageSender);
            this.messageReceiver = new MessageReceiver(new DataInputStream(this.is), this.getClient().getClientInfo());
            this.messageReceiverThread = new Thread(this.messageReceiver);

            this.messageSenderThread.start();
            this.messageReceiverThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
