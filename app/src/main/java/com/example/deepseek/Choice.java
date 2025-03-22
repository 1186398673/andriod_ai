package com.example.deepseek;

import com.google.gson.annotations.SerializedName;

public class Choice {
    @SerializedName("index")
    private int index;

    @SerializedName("message")
    private MessageResponse message;

    @SerializedName("finish_reason")
    private String finishReason;

    @SerializedName("logprobs")
    private Object logprobs; // 可以根据需要更改为具体的类型

    @Override
    public String toString() {
        return "Choice{" +
                "index=" + index +
                ", message=" + message +
                ", finishReason='" + finishReason + '\'' +
                ", logprobs=" + logprobs +
                '}';
    }

    // Getters and Setters
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public MessageResponse getMessage() {
        return message;
    }

    public void setMessage(MessageResponse message) {
        this.message = message;
    }

    public String getFinishReason() {
        return finishReason;
    }

    public void setFinishReason(String finishReason) {
        this.finishReason = finishReason;
    }

    public Object getLogprobs() {
        return logprobs;
    }

    public void setLogprobs(Object logprobs) {
        this.logprobs = logprobs;
    }
}