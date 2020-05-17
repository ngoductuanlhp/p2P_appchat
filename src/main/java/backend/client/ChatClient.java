package backend.client;

import ui.MainUI;
import utils.ClientInfo;
import utils.PeerInfo;

import backend.client.*;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.LinkedList;
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

    private ClientInfo clientInfo;

    private HashMap<String, PeerHandler> peerList;

    private String responseMessage = null;

    public String getResponseMessage() {return this.responseMessage;}
    public void setResponseMessage(String mess) {this.responseMessage = mess;}

    private int peerPort = 20000;
//    private MainUI mainUI;

    public ChatClient(String serverAddress, int serverPort) {
        this.serverAddress  = serverAddress;
        this.serverPort     = serverPort;
//        this.mainUI         = mainUI;
    }

    public ClientInfo getClientInfo() {
        return this.clientInfo;
    }

    public RequestSender getRequestSender() { return requestSender; }
    public HashMap<String, PeerHandler>  getPeerList() {return this.peerList;}

    public void start() {
        System.out.println("[CLIENT] Start client.");
        boolean checkConnected = this.connectToServer();
        if (!checkConnected) {
            System.out.println("[CLIENT] Cannot connect to server. Quit.");
            return;
        }
        this.peerList = new HashMap<>();
        this.requestSender = new RequestSender(this, new DataOutputStream((this.os)));
        this.clientSenderThread = new Thread(this.requestSender);
        this.requestReceiver = new RequestReceiver(this, new DataInputStream(this.is));
        this.clientReceiverThread = new Thread(this.requestReceiver);

        this.clientReceiverThread.start();
        this.clientSenderThread.start();

        // DEBUG
//        try{
//            BufferedReader keyboardReader = new BufferedReader(new InputStreamReader(System.in));
//            while (true) {
//                String req = keyboardReader.readLine();
//                this.requestSender.sendRequest(req);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public boolean sendReq(String request) {
        return this.requestSender.sendRequest(request);
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


    public void checkSignUp(String check, String name) {
        if (check.equals("success")) {
            System.out.println("[CLIENT] Sign-up successful");
            this.clientInfo = new ClientInfo(name);
        }
        else {
            System.out.println("[CLIENT] Sign-up failed");
        }
    }

    public void checkLogIn(String[] segments) {
        if (segments[1].equals("success")) {
            System.out.println("[CLIENT] Log-in successful");
            this.clientInfo = new ClientInfo(segments[2]);
            int numFriend = Integer.parseInt(segments[3]);
//            System.out.println(segments);
            if (numFriend > 0) {
                for(int i = 0; i < numFriend; i++) {
                    this.clientInfo.friendList.add(new PeerInfo(segments[4 + 2*i], segments[4 + 2*i + 1]));
                }
            }
            synchronized (this) {
                this.responseMessage = segments[1] + "-" + "login";
            }

        }
        else {
            System.out.println("[CLIENT] Log-in failed");
        }
    }

    public void checkLogOut(String check) {
        if (check.equals("success")) {
            this.clientInfo = null;
            this.peerList = null;
        }
    }

    public void checkAddFriend(String check, String friendName, String status) {
        if (check.equals("success")) {
            this.clientInfo.friendList.add(new PeerInfo(friendName, status));
        }
    }

    public void peerConnectListener(String nameFrom, String nameTo) {
        boolean isExist = false;
        if (peerList.containsKey(nameFrom)) {
            isExist = true;
        }
//        for(PeerHandler peer:this.peerList) {
//            if(nameFrom.equals(peer.getTargetClientName())) {
//                isExist = true;
//                break;
//            }
//        }
        if (isExist) {
            return;
        }

        try {
            ServerSocket serverSocket = new ServerSocket(peerPort);
            String mess = "acceptconnectfriend-" + nameFrom + "-" + nameTo + "-" + String.valueOf(peerPort);
            this.requestSender.sendRequest(mess);
            peerPort++;
            Socket socket = serverSocket.accept();
            PeerHandler peerHandler = new PeerHandler(socket, nameFrom, this);
//            this.peerList.add(peerHandler);
            this.peerList.put(nameFrom, peerHandler);
            Thread peerThread = new Thread(peerHandler);
            peerThread.start();
        } catch (IOException e) {
            e.printStackTrace();
            peerPort++;
            this.peerConnectListener(nameFrom, nameTo);
        }
    }

    public void peerConnectActivator(String nameFrom, String nameTo, String IP, int port, int timeTry) {
//        boolean isExist = false;
//        for(PeerHandler peer:this.peerList) {
//            if(targetName.equals(peer.getTargetClientName())) {
//                isExist = true;
//                break;
//            }
//        }
//        if (isExist) {
//            return;
//        }

        try {
            System.out.println("IP:" + IP);
            // backend
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(IP, port), 5000);

            PeerHandler peerHandler = new PeerHandler(socket, nameTo, this);

            // JTextPane

//            this.peerList.add(peerHandler);
            this.peerList.put(nameTo, peerHandler);
            Thread peerThread = new Thread(peerHandler);
            peerThread.start();
            synchronized (this) {
                this.responseMessage = "success-" + nameTo;
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (timeTry < 5) {
                timeTry++;
                this.peerConnectActivator(nameFrom, nameTo, IP, port, timeTry);
            }
        }
    }

    public void removePeerHandle(PeerHandler peerHandler) {
        this.peerList.remove(peerHandler);
    }

}
