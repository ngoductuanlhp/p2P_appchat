package client;

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
                            break;
                        case "login":
                            break;
                        case "logout":
                            break;
                        case "addfriend":
                            break;
                        case "connectfriend":
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
