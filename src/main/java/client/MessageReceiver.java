package client;

import org.apache.commons.lang3.StringUtils;
import utils.ClientInfo;
import utils.Message;

import java.io.DataInputStream;
import java.io.IOException;

public class MessageReceiver implements Runnable {
    private ClientInfo targetClient;
    private DataInputStream reader;

    public MessageReceiver(DataInputStream reader, ClientInfo targetClient) {
        this.reader = reader;
        this.targetClient = targetClient;
    }


    @Override
    public void run() {
        try {
            String message      = null;
            String[] segments   = null;
            while (true) {
                message = this.reader.readUTF();
                segments = StringUtils.split(message, '-');
                if (segments != null && segments.length > 0) {
                    String type = segments[0];
                    switch (type) {
                        case "message":
                            Message mess = new Message(segments[1], this.targetClient.getClientName());
                            break;
                        case "file":
                            break;
                        default:
                            System.out.println("[CLIENT] Received unknown message.");
                            break;
                    }

                }
            }
        } catch (IOException e) {
        e.printStackTrace();
    }
    }
}
