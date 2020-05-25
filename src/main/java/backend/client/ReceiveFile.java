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
import utils.FileInfo;

/**
 *
 * @author Khoa
 */
public class ReceiveFile extends Thread{
    private MessageSender sender;
    private int portReceiveFile = 5678;
    private ObjectInputStream ois = null;
    private ObjectOutputStream oos = null;
    
    public ReceiveFile(MessageSender send_mess){
        this.sender = send_mess;
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
        ServerSocket serverSocket = new ServerSocket(portReceiveFile);
        System.out.println(Inet4Address.getLocalHost().getHostAddress().trim() +" " + Integer.toString(serverSocket.getLocalPort()));
        this.sender.sendMessage("AcceptSendFile" +"-"+ Inet4Address.getLocalHost().getHostAddress().trim()+"-" + Integer.toString(portReceiveFile));
        
        Socket client = serverSocket.accept();
        
        receivingFile(client);
        
        client.close();
        serverSocket.close();
        System.out.println("Done receiving file from friend");
    }
    
    private void receivingFile(Socket client) throws IOException{
        try {
                // receive file info
                ois = new ObjectInputStream(client.getInputStream());
                FileInfo fileInfo = (FileInfo) ois.readObject();
                if (fileInfo != null) {
                    createFile(fileInfo);
                }
                  // confirm that file is received
//                oos = new ObjectOutputStream(client.getOutputStream());
//                fileInfo.setStatus("success");
//                fileInfo.setDataBytes(null);
//                oos.writeObject(fileInfo);
                  
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
                if (name_os.equals("mac os x")){
                    dir = dir + "Downloads/";
                }
                else if (name_os.equals("windows 10"))
                {
                    dir += "Downloads\\";
                }
                File fileReceive = new File(dir + fileInfo.getFilename());
                bos = new BufferedOutputStream(
                        new FileOutputStream(fileReceive));
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
    
}
