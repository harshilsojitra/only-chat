package com.example.onch.ModelClass;

public class Users {

    String Uid;
    public String name;
    String email;
    public String imageUri;
    public String status;

    public Users() {

    }

    public Users(String uid, String name, String email, String imageUri) {
        Uid = uid;
        this.name = name;
        this.email = email;
        this.imageUri = imageUri;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
