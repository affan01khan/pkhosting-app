package com.pkhosting.app.models;

import java.io.Serializable;

/**
 * A support ticket from the Firestore "tickets" collection.
 * status is "Open" or "Resolved". Replies live in the "replies" sub-collection.
 */
public class Ticket implements Serializable {

    public static final String STATUS_OPEN = "Open";
    public static final String STATUS_RESOLVED = "Resolved";

    private String id = "";
    private String userId = "";
    private String subject = "";
    private String category = "";
    private String message = "";
    private String status = "";
    private long createdAt = 0;

    public Ticket() {
        // Required empty constructor for Firestore deserialization.
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}
