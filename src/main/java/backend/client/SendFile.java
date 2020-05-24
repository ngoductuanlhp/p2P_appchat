/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend.client;

/**
 *
 * @author Khoa
 */
public class SendFile extends Thread{
    private int state; // 0 waiting , 1 send file
    private String filepath;
    private String filename;
    
    public SendFile(){
        state = 0;
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
                    sendFile();
                    break;
            }
        }
    }
    
    private void sendFile(){
        this.state = 0;
    }
    
    private void waiting(){
        
    }
    
    public void startSendFile(){
        this.state = 1;
    }

    public void setFilePath(String path) {
        this.filepath = path;
    }

    public void setFileName(String name)
    {
        this.filename = name;
    }
    
    
    
}
