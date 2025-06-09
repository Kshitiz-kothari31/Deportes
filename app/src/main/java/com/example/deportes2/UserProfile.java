package com.example.deportes2;

public class UserProfile {
    private String id;
    private String name;
    private String profileImageUrl;

    public UserProfile(String id, String name, String profileImageUrl) {
        this.id = id;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }
}

