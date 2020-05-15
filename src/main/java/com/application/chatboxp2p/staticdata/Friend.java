/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.application.chatboxp2p.staticdata;

/**
 *
 * @author Khoa
 */
public class Friend {
    private int status;
    private String name;
    private String user_name;
    
    public Friend(String name ,String user_name , int status ){
        this.name = name;
        this.status = status;
        this.user_name = user_name;
        
    }

    
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
    
    
    
}
