package backend.client;

import utils.ClientInfo;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
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

    private JTextPane textPane;

    public PeerHandler(Socket socket, String targetClientName) {
        this.socket = socket;
        this.targetClientName = targetClientName;
        this.textPane = new JTextPane();
        textPane.setSize(610, 370);
    }

    public JTextPane getTextPane() {return this.textPane;}

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
            this.messageReceiver = new MessageReceiver(new DataInputStream(this.is), this.getClient().getClientInfo(), this);
            this.messageReceiverThread = new Thread(this.messageReceiver);

            this.messageSenderThread.start();
            this.messageReceiverThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String mess) {
        String formattedMess = "message-" + mess;
        this.messageSender.sendMessage(formattedMess);
    }

    public void sendDisconnect() {
        this.messageSender.sendMessage("disconnect");
    }

    public void disconnect() {
        this.messageSender.stop();
        this.messageReceiver.stop();
        this.client.removePeerHandle(this);
    }

    public void addText(String mess) throws BadLocationException {
        StyledDocument doc = this.textPane.getStyledDocument();
        Style style1 = this.textPane.addStyle("myStyle", null);
        Style style2 = this.textPane.addStyle("myStyle", null);
        JLabel label = new JLabel(this.targetClientName + ":");
        label.setFont(new java.awt.Font("Times New Roman", 1, 16));
        label.setForeground(new java.awt.Color(28, 160, 28));
        StyleConstants.setComponent(style1, label);

        JTextArea textArea = new JTextArea(mess);
        textArea.setLineWrap(true);
        textArea.setFont(new java.awt.Font("Times New Roman", 1, 14));

        StyleConstants.setComponent(style2, textArea);
        synchronized (this) {
            doc.insertString(doc.getLength(), " ", style1);
            doc.insertString(doc.getLength(), "\n", style2);
        }

    }

}
