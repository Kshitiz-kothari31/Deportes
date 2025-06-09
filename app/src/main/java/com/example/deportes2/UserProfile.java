package com.example.deportes2;

public class UserProfile {
    public String id;
    public String name;
    public String profileImageUrl;

    public UserProfile() {
    }

    public UserProfile(String id, String name, String profileImageUrl) {
        this.id = id;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
    }
}
