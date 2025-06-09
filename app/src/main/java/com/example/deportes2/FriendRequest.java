package com.example.deportes2;

public class FriendRequest {
    private String id;
    private String sender_id;
    private String receiver_id;
    private String status;

    public FriendRequest(String id, String sender_id, String receiver_id, String status) {
        this.id = id;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.status = status;
    }

    // ✅ Getter methods
    public String getId() {
        return id;
    }

    public String getSenderId() {
        return sender_id;
    }

    public String getReceiverId() {
        return receiver_id;
    }

    public String getStatus() {
        return status;
    }

    // Optional: Add setters if you need to modify the object
}
