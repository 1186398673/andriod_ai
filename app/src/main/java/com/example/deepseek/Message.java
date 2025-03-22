package com.example.deepseek;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Message {
    @SerializedName("role")
    private String role;
    @SerializedName("content")
    private String content;

    private String timestamp;


    public Message(String role, String content) {
        this.role = role;
        this.content = content;
        this.timestamp = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
    }

    // Getters and Setters
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() { return timestamp; }
}