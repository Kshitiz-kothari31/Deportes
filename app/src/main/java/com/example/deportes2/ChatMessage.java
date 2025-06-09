package com.example.deportes2;

public class ChatMessage {
    private String id;
    private String senderId;
    private String receiverId;
    private String message;
    private String createdAt;
    private boolean isSeen;

    public ChatMessage(String id, String senderId, String receiverId, String message, String createdAt) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.createdAt = createdAt;
    }

    public String getId() { return id; }
    public String getSenderId() { return senderId; }
    public String getReceiverId() { return receiverId; }
    public String getMessage() { return message; }
    public String getCreatedAt() { return createdAt; }
    public boolean isSeen() { return isSeen; }
}
