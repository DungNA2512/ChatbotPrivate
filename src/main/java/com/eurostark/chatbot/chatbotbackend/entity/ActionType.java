package com.eurostark.chatbot.chatbotbackend.entity;

public enum ActionType {

    SAVE_LEAVE,
    CREATE_TICKET,
    SHOW_COMPANY_INFO,
    SHOW_COMPANY_ADDRESS,
    SHOW_COMPANY_HOURS,
    SHOW_COMPANY_CONTACT,
    SEND_DOCUMENT;

    public static ActionType from(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        try {
            return ActionType.valueOf(raw.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}