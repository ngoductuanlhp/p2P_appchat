package client;

import utils.ClientInfo;

import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.Queue;

public class MessageSender implements Runnable{
    private ClientInfo hostClient;
    private DataOutputStream sender;
    private Queue<String> messageQueue = new LinkedList<String>();
    public MessageSender(DataOutputStream sender, ClientInfo hostClient) {
        this.sender = sender;
        this.hostClient = hostClient;
    }


    @Override
    public void run() {
        try{
            while (true) {
                synchronized (this) {
                    while(!this.messageQueue.isEmpty()) {
                        String request = messageQueue.remove();
                        System.out.print(String.format("[CLIENT] Send message: %s", request));
                        sender.writeUTF(request);
                        sender.flush();

                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String cnt) {
        String mess = cnt + "-" + this.hostClient.getClientName() + "-" + new Timestamp(System.currentTimeMillis()).toString();
        synchronized (this) {
            this.messageQueue.add(mess);
        }
    }
}
