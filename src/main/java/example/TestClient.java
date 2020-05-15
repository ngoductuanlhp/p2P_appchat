package example;

import client.ChatClient;
import server.ChatServer;

import java.beans.XMLDecoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class TestClient {

    public static void main(String[] args) throws FileNotFoundException {
//        ChatClient chatClient = new ChatClient("localhost", 11200);
//        chatClient.start();
        File folder = new File("/home/tuan/2d_laser_detect");
        File[] listFile = folder.listFiles();
        for(File f:listFile) {
            System.out.println(f.getName());
            FileInputStream fis = new FileInputStream(f);
            XMLDecoder x = new XMLDecoder(fis);
            String string = (String)x.readObject();
            System.out.println(string);
        }
    }
}
