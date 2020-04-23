package com.example.ecoleenligne.data;

public class NetworkMessage {
    private String message;

    public NetworkMessage(){
        this.message = "";
    }

    public NetworkMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
