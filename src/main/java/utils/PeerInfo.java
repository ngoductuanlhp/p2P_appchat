package utils;

public class PeerInfo {
    private String name;
    private String status;

    public  PeerInfo(String name) {
        this.name = name;
        this.status = "offline";
    }
    public  PeerInfo(String name, String status) {
        this.name = name;
        this.status = status;
    }

    public String getName() {return this.name;}
    public String getStatus(){return this.status;}
    public void setOffline() {this.status = "offline";}
    public void setOnline() {this.status = "online";}
}
