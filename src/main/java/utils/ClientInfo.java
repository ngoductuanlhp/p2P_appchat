package utils;

// This class use for Server to maintain a list of clients connected

public class ClientInfo {
    private String clientName;
    private String clientStatus;
    private String clientID;

    public ClientInfo(String clientName, String clientStatus, String clientID) {
        this.clientName     = clientName;
        this.clientStatus   = clientStatus;
        this.clientID       = clientID;
    }

    public void setClientName(String clientName) {this.clientName = clientName;}
    public String getClientName() {return this.clientName;}

    public void setClientStatus(String clientStatus) {this.clientStatus = clientStatus;}
    public String getClientStatus() {return this.clientStatus;}

    public void setClientID(String clientID) {this.clientID = clientID;}
    public String getClientID() {return this.clientID;}
}
