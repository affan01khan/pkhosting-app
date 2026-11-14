package com.pkhosting.app.models;

/**
 * One reply inside the "replies" sub-collection of a support ticket.
 */
public class TicketReply {

    private String author = "";
    private String message = "";
    private long createdAt = 0;

    public TicketReply() {
        // Required empty constructor for Firestore deserialization.
    }

    public TicketReply(String author, String message, long createdAt) {
        this.author = author;
        this.message = message;
        this.createdAt = createdAt;
    }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}
