package com.example.deportes2;

public class UserProfile {
    private String id;
    private String name;
    private String username;
    private String profileImageUrl;

    public UserProfile(String id, String name, String profileImageUrl) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.profileImageUrl = profileImageUrl;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }
}

