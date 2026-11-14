package com.pkhosting.app.models;

/**
 * A knowledgebase entry from the Firestore "faqs" collection.
 */
public class Faq {

    private String id = "";
    private String question = "";
    private String answer = "";
    private String category = "";

    public Faq() {
        // Required empty constructor for Firestore deserialization.
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }
    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
