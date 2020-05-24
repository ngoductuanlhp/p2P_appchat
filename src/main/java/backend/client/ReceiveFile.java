/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Khoa
 */
public class ReceiveFile extends Thread{
    private int state;
    private MessageSender sender;
    private int portReceiveFile = 5678;
    public ReceiveFile(MessageSender send_mess){
        this.state = 0;
    }
    
    @Override
    public void run(){
        while (true){
            switch(state){
                case 0: 
                    break;
                case 1:
                    break;
            }
        }
    }
    
    private void waiting(){
        
    }
    private void receiving() throws IOException{
        ServerSocket socket = new ServerSocket(portReceiveFile);
        this.sender.sendMessage("AcceptSendFile");
        Socket client = socket.accept();
        
        
        System.out.println("Receiving file from friend");
        System.out.println("Done receiving file from friend");
        this.state = 0;
    }
    
    public void setState(int state){
        this.state = state;
    }
}
