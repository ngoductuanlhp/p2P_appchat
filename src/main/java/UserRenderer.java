
import com.application.chatboxp2p.staticdata.Friend;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Khoa
 */
public class UserRenderer extends DefaultListCellRenderer implements ListCellRenderer<Object>{
   
    public Component getListCellRendererComponent(JList<?> list , Object friend , int index , boolean isSelected ,boolean hasFocus){
        Friend f = (Friend) friend;
        setText(f.getName());
        if (f.getStatus() == 1) 
            setIcon(new javax.swing.ImageIcon("C:\\Users\\Khoa\\OneDrive\\Documents\\NetBeansProjects\\ChatBoxP2P\\src\\main\\java\\images\\online_icon.png"));
        else if (f.getStatus() == 0)setIcon(new javax.swing.ImageIcon("C:\\Users\\Khoa\\OneDrive\\Documents\\NetBeansProjects\\ChatBoxP2P\\src\\main\\java\\images\\offline_icon.png"));
        else setIcon(new javax.swing.ImageIcon("C:\\Users\\Khoa\\OneDrive\\Documents\\NetBeansProjects\\ChatBoxP2P\\src\\main\\java\\images\\notification_icon.png")); 
        setIconTextGap(10);
        if (isSelected){
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        }
        else{
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        setEnabled(true);
        setFont(list.getFont());
        return this;
    }
    
}
