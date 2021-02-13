package com.example.maplechat.models;

public class User {
    private String userName;
    private String email;
    private String imageUrl;

    public User() {
    }

    public User(String userName, String email, String imageUrl) {
        this.userName = userName;
        this.email = email;
        this.imageUrl = imageUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
