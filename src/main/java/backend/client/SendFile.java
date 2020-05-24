/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.client;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.FileInfo;

/**
 *
 * @author Khoa
 */
public class SendFile extends Thread{
    private static boolean allowSending;
    private String filepath;
    private String filename;
    private MessageSender sender;
    private int send_port;
    private String send_address;
    private ObjectOutputStream oos = null;
    private ObjectInputStream ois = null;
    
    public SendFile(MessageSender send_mess , String filename , String path){
        this.sender = send_mess;
        this.filename = filename;
        this.filepath = path;
        allowSending = false;
        System.out.println(this.filename + " " + this.filepath);
    }

    
    @Override
    public void run(){
        try {
            sending();
        } catch (IOException ex) {
            Logger.getLogger(SendFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void sending() throws IOException{
        sender.sendMessage("SendFile");
        while (allowSending == false){
            System.out.println(this.send_port);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(SendFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        Socket server = new Socket();
        server.connect(new InetSocketAddress(send_address, send_port), 5000);
        System.out.println("Sending File to friend...");
        
        sendingFile(server);
        
        server.close();
        System.out.println("Finish sending file to friend...");
    }
     
    
    public void allowSending(String address ,int port){
        allowSending = true;
        this.send_port = port;
        this.send_address = address;
    }
    
    private void sendingFile(Socket server){
        try {
            FileInfo fileInfo = getFileInfo(filepath, "C:\\Users\\Khoa\\Downloads\\");
            oos = new ObjectOutputStream(server.getOutputStream());
            oos.writeObject(fileInfo);
 
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            closeStream(oos);
            closeStream(ois);
        }
    }
    
    private FileInfo getFileInfo(String sourceFilePath, String destinationDir) {
        FileInfo fileInfo = null;
        BufferedInputStream bis = null;
        try {
            File sourceFile = new File(sourceFilePath);
            bis = new BufferedInputStream(new FileInputStream(sourceFile));
            fileInfo = new FileInfo();
            byte[] fileBytes = new byte[(int) sourceFile.length()];
            // get file info
            bis.read(fileBytes, 0, fileBytes.length);
            fileInfo.setFilename(sourceFile.getName());
            fileInfo.setDataBytes(fileBytes);
            fileInfo.setDestinationDirectory(destinationDir);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            closeStream(bis);
        }
        return fileInfo;
    }

    public void closeStream(InputStream inputStream) {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void closeStream(OutputStream outputStream) {
        try {
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
