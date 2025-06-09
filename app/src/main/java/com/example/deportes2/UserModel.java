package com.example.deportes2;

public class UserModel {
    private String id;
    private String name;
    private String username;
    private String profile_img;

    public UserModel(String id, String name, String username, String profileImageUrl) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.profile_img = profileImageUrl;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getProfileImg() {
        return profile_img;
    }

    // Optional: Setters (if you plan to modify these values later)
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profile_img = profileImageUrl;
    }
}
