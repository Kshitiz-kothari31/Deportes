package com.example.deportes2;

public class Post {

    private String id;
    private String user_id;
    private String content;
    private String image_url;
    private String created_at;

    private Profile profiles;

    private int likes;
    private int comments;
    private boolean isLikedByCurrentUser;

    // Nested Profile class
    public static class Profile {
        private String name;
        private String profile_img;

        public String getName() {
            return name;
        }

        public String getProfile_img() {
            return profile_img;
        }
    }

    // Empty constructor required for JSON deserialization
    public Post() {}

    // Full constructor (optional)
    public Post(String id, String user_id, String content, String image_url, String created_at) {
        this.id = id;
        this.user_id = user_id;
        this.content = content;
        this.image_url = image_url;
        this.created_at = created_at;
    }

    // Getters and setters for post fields
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    // Getter and setter for profiles
    public Profile getProfiles() {
        return profiles;
    }

    public void setProfiles(Profile profiles) {
        this.profiles = profiles;
    }

    // Convenience method to get user name safely
    public String getUser_name() {
        return profiles != null ? profiles.getName() : "Unknown";
    }

    // Convenience method to get profile image url safely
    public String getProfile_image_url() {
        return profiles != null ? profiles.getProfile_img() : null;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public boolean isLikedByCurrentUser() {
        return isLikedByCurrentUser;
    }

    public void setLikedByCurrentUser(boolean likedByCurrentUser) {
        isLikedByCurrentUser = likedByCurrentUser;
    }

}
