package edu.university.livechat.data.model;

import androidx.annotation.NonNull;

public class Message {
    private Long id;
    private String content;
    private String sender;
    private String destination;
    private String time;

    private String type;
    public Message(String content) {
        this.content = content;
    }


    public Message(String content, String sender, String destination, String timestamp) {
        this.content = content;
        this.sender = sender;
        this.destination = destination;

        //timestamp is of format 2023-05-13T19:02:21.032+00:00
        // convert it to Month Day, Time:minutes AM/PM
        String[] parts = timestamp.split("T");
        String[] date = parts[0].split("-");
        String[] time = parts[1].split(":");
        String[] seconds = time[2].split("\\.");
        String[] hour = time[0].split(":");
        int hourInt = Integer.parseInt(hour[0]);
        String ampm = "AM";
        if(hourInt > 12){
            hourInt = hourInt - 12;
            ampm = "PM";
        }
        String newTimestamp = date[1] + "/" + date[2] + "/" + date[0] + ", " + hourInt + ":" + time[1] + " " + ampm;
        this.time = newTimestamp;
    }

    public Message(String content, String sender, String destination) {
        this.content = content;
        this.sender = sender;
        this.destination = destination;
    }

    public Message(String destination, String message) {
        this.destination = destination;
        this.content = message;
        this.type = "CHAT";
    }

    // getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getTimestamp() {
        return time;
    }

    public void setTimestamp(String time) {
        this.time = time;
    }

    @NonNull
    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", sender='" + sender + '\'' +
                ", destination='" + destination + '\'' +
                ", timestamp='" + time + '\'' +
                '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
