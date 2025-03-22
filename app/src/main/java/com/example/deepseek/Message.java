package com.example.deepseek;

import com.google.gson.annotations.SerializedName;

public class Message {
    @SerializedName("role")
    private String role;
    @SerializedName("content")
    private String content;


    public Message(String role, String content) {
        this.role = role;
        this.content = content;
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
}