package com.sacavix.chatgpt;

public class ChatEntry {
    private String question;
    private String response;

    // Constructor, getters y setters

    public ChatEntry(String question, String response) {
        this.question = question;
        this.response = response;
    }

    // Getters y setters
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
    
}
