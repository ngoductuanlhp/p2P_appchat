package utils;

import java.sql.Timestamp;

public class Message {
    private String content;
    private String name_from;
    private String name_to;
    private Timestamp time_send;
    private Timestamp time_receive;

    public Message(String name_f, String name_t, Timestamp time_s, Timestamp time_r) {
        this.name_from = name_f;
        this.name_to = name_t;
        this.time_send = time_s;
        this.time_receive = time_r;
    }

    public Message(String cnt, String name_f, String name_t, Timestamp time_s, Timestamp time_r) {
        this.content = cnt;
        this.name_from = name_f;
        this.name_to = name_t;
        this.time_send = time_s;
        this.time_receive = time_r;
    }

    public void setContent(String cnt) {
        this.content = cnt;
    }

    public String getContent() {
        return this.content;
    }

    public void setName_from(String name) {
        this.name_from = name;
    }

    public String getName_from() {
        return this.name_from;
    }

    public void setName_to(String name) {
        this.name_to = name;
    }

    public String getName_to() {
        return this.name_to;
    }

    public void setTime_send(Timestamp t) {
        this.time_send = t;
    }

    public Timestamp getTime_send() {
        return this.time_send;
    }

    public void setTime_receive(Timestamp t) {
        this.time_receive = t;
    }

    public Timestamp getTime_receive() {
        return this.time_receive;
    }
}
