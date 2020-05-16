package backend.client;

import org.apache.commons.lang3.StringUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RequestReceiver implements Runnable {
    private ChatClient chatClient;
    private DataInputStream reader;


    public RequestReceiver(ChatClient chatClient, DataInputStream reader) {
        this.chatClient = chatClient;
        this.reader = reader;
    }

    @Override
    public void run() {
        try{
            String message      = null;
            String[] segments   = null;
            while (true) {
                message= this.reader.readUTF();
                segments = StringUtils.split(message,'-');
                if (segments != null && segments.length > 0) {
                    String type = segments[0];
                    switch (type) {
                        case "signup":
                            this.chatClient.checkSignUp(segments[1], segments[2]);
                            break;
                        case "login":
                            System.out.println(message);
                            this.chatClient.checkLogIn(segments);

                            break;
                        case "logout":
                            this.chatClient.checkLogOut(segments[1]);
                            break;
                        case "addfriend":
                            this.chatClient.checkAddFriend(segments[1], segments[2], segments[3]);
                            break;
                        case "connectfriendto":
                            this.chatClient.peerConnectListener(segments[1], segments[2]);;
                            break;
                        case "acceptconnectfriend":
                            this.chatClient.peerConnectActivator(segments[1], segments[2], segments[3], Integer.parseInt(segments[4]), 0);
                            break;
                        default:
                            System.out.println("Unknown " + type);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
