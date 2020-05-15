/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.application.chatboxp2p.staticdata;

import java.awt.List;
import javax.swing.DefaultListModel;
import org.w3c.dom.ls.LSInput;

/**
 *
 * @author Khoa
 */
public class ListFriends {
    private static DefaultListModel<Friend> list_friends = new DefaultListModel<>();
    
    public static void addUser(Friend user){
        list_friends.addElement(user);
    }
    public static DefaultListModel getListModel()
    {
        return list_friends;
    }
    public static void clear()
    {
        list_friends.clear();
    }
    public Friend getUserByIndex(int index){
        return list_friends.get(index);
    }
    public static void updateStatus(String user_name , int status){
        for (int i = 0; i<list_friends.getSize() ; i++){
            if (list_friends.get(i).getUser_name().equals(user_name))
            {
                list_friends.get(i).setStatus(status);
                break;
            }
        }
    }
    public static void updateStatus(int index , int status){
        list_friends.get(index).setStatus(status);
    }
}
