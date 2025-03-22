package com.example.deepseek;

import com.google.gson.annotations.SerializedName;

public class ChatRequest {

    @SerializedName("model")
    private String model;

    @SerializedName("messages")
    private Message[] messages;

    @SerializedName("stream")
    private boolean stream;


    public ChatRequest(String model, Message[] messages, boolean stream) {
        this.model = model;
        this.messages = messages;
        this.stream = stream;
    }

    // Getters and Setters
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Message[] getMessages() {
        return messages;
    }

    public void setMessages(Message[] messages) {
        this.messages = messages;
    }

    public boolean isStream() {
        return stream;
    }

    public void setStream(boolean stream) {
        this.stream = stream;
    }
}