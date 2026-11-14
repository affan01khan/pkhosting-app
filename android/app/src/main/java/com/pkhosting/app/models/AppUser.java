package com.pkhosting.app.models;

/**
 * User profile document from the Firestore "users" collection.
 * The document id is always the Firebase Auth uid.
 */
public class AppUser {

    private String uid = "";
    private String name = "";
    private String email = "";
    private String phone = "";
    private long createdAt = 0;

    public AppUser() {
        // Required empty constructor for Firestore deserialization.
    }

    public AppUser(String uid, String name, String email, String phone, long createdAt) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.createdAt = createdAt;
    }

    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}
