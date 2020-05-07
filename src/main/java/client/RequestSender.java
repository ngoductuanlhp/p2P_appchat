package client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class RequestSender implements Runnable{
    private ChatClient chatClient;
    private DataOutputStream sender;
    private Queue<String> requestQueue = new LinkedList<String>();

    public RequestSender(ChatClient chatClient, DataOutputStream sender) {
        this.chatClient = chatClient;
        this.sender = sender;
    }
    @Override
    public void run() {
        try{
            while (true) {
                synchronized (this) {
                    while(!this.requestQueue.isEmpty()) {
                        String request = requestQueue.remove();
                        System.out.print(String.format("[CLIENT] Send request: %s", request));
                        sender.writeUTF(request);
                        sender.flush();

                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean sendRequest(String request) {
        synchronized (this) {
            return this.requestQueue.add(request);
        }
    }
}
