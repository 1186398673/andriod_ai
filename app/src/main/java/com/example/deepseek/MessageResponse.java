package com.example.deepseek;

import com.google.gson.annotations.SerializedName;

public class MessageResponse {
    @SerializedName("role") // 直接映射message对象中的role字段
    private String role;
    
    @SerializedName("content") // 直接映射message对象中的content字段
    private String content;

    @Override
    public String toString() {
        return "MessageResponse{" +
                "role='" + role + '\'' +
                ", content='" + content + '\'' +
                '}';
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
