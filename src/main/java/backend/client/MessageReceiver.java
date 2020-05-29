package backend.client;

import org.apache.commons.lang3.StringUtils;
import utils.ClientInfo;
import utils.Message;

import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;

public class MessageReceiver implements Runnable {
    private ClientInfo targetClient;
    private int portReceiveFile = 5678;
    private DataInputStream reader;
    private PeerHandler peerHandler;
    private volatile boolean isChatting = true;

    public MessageReceiver(DataInputStream reader, ClientInfo targetClient, PeerHandler peerHandler) {
        this.reader = reader;
        this.targetClient = targetClient;
        this.peerHandler = peerHandler;
    }

    @Override
    public void run() {
        try {
            String message      = null;
            String[] segments   = null;
            while (isChatting) {
                message = this.reader.readUTF();
                segments = StringUtils.split(message, '-');
                if (segments != null && segments.length > 0) {
                    String type = segments[0];
                    switch (type) {
                        case "message":
//                            Message mess = new Message(segments[1], this.targetClient.getClientName());
                            String mess = segments[1];
                            this.peerHandler.addText(mess);
                            break;
                        case "SendFile":
                            System.out.println("[Friend] request send file");
                            this.peerHandler.receiveFile();
                            break;
                        case "AcceptSendFile":
                            this.peerHandler.allowSending();
                            break;
                        case "RejectSendFile":
                            this.peerHandler.rejectSending();
                            break;
                        case "TimeOutReceiveFile":
                            this.peerHandler.timeOutReceiveFile();
                            break;
                        case "disconnect":
                            this.peerHandler.disconnect();
                        default:
                            System.out.println("[CLIENT] Received unknown message.");
                            break;
                    }
                }
            }
        } catch (IOException | BadLocationException e) {
            e.printStackTrace();
        }
    }



    public void stop() {
        this.isChatting = false;
    }
    
}
