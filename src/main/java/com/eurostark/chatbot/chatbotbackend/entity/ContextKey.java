package com.eurostark.chatbot.chatbotbackend.entity;

public enum ContextKey {
    SESSION_ID("sessionId"),
    LEAVE_DATE("leaveDate"),
    DOCUMENT_CODE("documentCode"),
    ACTION_EXECUTED("actionExecuted");

    private final String key;

    ContextKey(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }
}
