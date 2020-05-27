/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.client;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import utils.FileInfo;

/**
 *
 * @author Khoa
 */
public class ReceiveFile extends Thread{
    private MessageSender sender;
    private int portReceiveFile = 56789;
    private ObjectInputStream ois = null;
    private ObjectOutputStream oos = null;
    private boolean time_out;
    public ReceiveFile(MessageSender send_mess){
        this.sender = send_mess;
        time_out = true;
    }
    
    @Override
    public void run(){
        try {
            receiving();
        } catch (IOException ex) {
            Logger.getLogger(ReceiveFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void receiving() throws IOException{
        int accept = JOptionPane.showConfirmDialog(null, "Accept file?", "Send file", JOptionPane.YES_NO_OPTION);
        if (accept == 0 || time_out){
            ServerSocket serverSocket = new ServerSocket(portReceiveFile);
            this.sender.sendMessage("AcceptSendFile");

            System.out.println("Waiting for connect");
            Socket client = serverSocket.accept();
            System.out.println("Accepted connection");

            receivingFile(client);

            client.close();
            serverSocket.close();
            System.out.println("Done receiving file from friend");
        }
        else this.sender.sendMessage("RejectSendFile");
    }
    
    private void receivingFile(Socket client) throws IOException{
        try {
                // receive file info
                ois = new ObjectInputStream(client.getInputStream());
                FileInfo fileInfo = (FileInfo) ois.readObject();
                if (fileInfo != null) {
                    createFile(fileInfo);
                }
                  
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                closeStream(ois);
                closeStream(oos);
            }                                       
    }
    
    private boolean createFile(FileInfo fileInfo) throws IOException {
        BufferedOutputStream bos = null;
         
        try {
            if (fileInfo != null) {
                String home = System.getProperty("user.home");
                String name_os = System.getProperty("os.name").toLowerCase();
                String dir = home;
                if (name_os.equals("windows 10"))
                {
                    dir += "\\Downloads\\";
                }
                else{
                    dir = dir + "/Downloads/";
                }
                File fileReceive = new File(dir + fileInfo.getFilename());
                bos = new BufferedOutputStream(new FileOutputStream(fileReceive));
                // write file content
                bos.write(fileInfo.getDataBytes());
                bos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeStream(bos);
        }
        return true;
    }
 
    public void closeStream(InputStream inputStream) throws IOException {
        if (inputStream != null) {
            inputStream.close();
        }
    }
 
    public void closeStream(OutputStream outputStream) throws IOException {
        if (outputStream != null) {
                outputStream.close();
        }
    }
    
    public void timeOut(){
        this.time_out = false;
    }
    
}
