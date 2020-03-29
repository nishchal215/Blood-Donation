package com.example.blooddonation;

public class User {

    private String email,userId,imageURL;

    public User() {

    }

    public User(String email, String userId, String imageURL) {
        this.email = email;
        this.userId = userId;
        this.imageURL = imageURL;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
