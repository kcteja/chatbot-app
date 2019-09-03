package com.example.test.chatbot.models;

import com.google.gson.annotations.SerializedName;

public class Message {

    public enum MessageType {SENDER, RECEIVER}

    ;

    @SerializedName("chatBotID")
    private int id;
    @SerializedName("chatBotName")
    private String name;
    @SerializedName("message")
    private String message;
    private MessageType type;
    @SerializedName("emotion")
    private String emotion;

    public Message() {
    }

    public Message(String message, MessageType type) {
        this.name = "KCT";
        this.message = message;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(String type) {
        if (type.equalsIgnoreCase("sender")) {
            this.type = MessageType.SENDER;
        } else {
            this.type = MessageType.RECEIVER;
        }
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }
}
