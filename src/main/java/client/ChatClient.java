package client;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

public class ChatClient {
    private final String serverAddress;
    private final int serverPort;
    private Socket socket;

    private InputStream is;
    private OutputStream os;
    private ServerSocket serverSocket;

    private RequestSender requestSender;
    private RequestReceiver requestReceiver;
    private Thread clientSenderThread;
    private Thread clientReceiverThread;

    public ChatClient(String serverAddress, int serverPort) {
        this.serverAddress  = serverAddress;
        this.serverPort     = serverPort;
    }

    public void start() {
        System.out.println("[CLIENT] Start client.");
        boolean checkConnected = this.connectToServer();
        if (!checkConnected) {
            System.out.println("[CLIENT] Cannot connect to server. Quit.");
            return;
        }

        this.requestSender = new RequestSender(this, new DataOutputStream((this.os)));
        this.clientSenderThread = new Thread(this.requestSender);
        this.requestReceiver = new RequestReceiver(this, new DataInputStream(this.is));
        this.clientReceiverThread = new Thread(this.requestReceiver);

        this.clientReceiverThread.start();
        this.clientSenderThread.start();

        // DEBUG
        try{
            BufferedReader keyboardReader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String req = keyboardReader.readLine();
                this.requestSender.sendRequest(req);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean connectToServer(){
        int countConnect = 0;
        System.out.println("[CLIENT] Trying connecting to server ...");
        do {
            try {
                this.socket = new Socket();
                this.socket.connect(new InetSocketAddress(serverAddress, serverPort), 5000);
                this.is = this.socket.getInputStream();
                this.os = this.socket.getOutputStream();
                System.out.print(String.format("[CLIENT] Successful connect to address %s port %d\n", this.serverAddress, this.serverPort));
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                countConnect += 1;
                System.out.println("[CLIENT] Waiting 1 second ...");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        } while(countConnect < 5);
        return false;

    }
}
