/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Khoa
 */
public class SendFile extends Thread{
    private int state; // 0 waiting , 1 send file , 2 receive file
    private boolean allowSending = false;
    private String filepath;
    private String filename;
    private MessageSender sender;
    
    public SendFile(MessageSender send_mess){
        state = 0;
        this.sender = send_mess;
    }

    
    @Override
    public void run(){
        while (true)
        {
            switch(state){
                case 0:
                    waiting();
                    break;
                case 1:
                    sending();
                    break;
            }
        }
    }
    
    private void sending(){
        sender.sendMessage("SendFile");
        while (allowSending == false){
            
        }
        System.out.println("Sending File to friend...");
        
        
        System.out.println("Finish sending file to friend...");
        this.allowSending  = false;
        this.state = 0;
    }
    
    private void waiting(){
        
    }
    public void setFilePath(String path) {
        this.filepath = path;
    }

    public void setFileName(String name)
    {
        this.filename = name;
    }
    
    public void allowSending(){
        allowSending = true;
    }
    
    public void setState(int state){
        this.state = state;
    }
    
    
    
}
