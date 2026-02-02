package com.eurostark.chatbot.chatbotbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatMessage {
    private String type; // TEXT | DOCUMENT
    private String content; // ✅ KHỚP frontend
    private String title;
    private String fileName;
    private String downloadUrl;

    public static ChatMessage text(String content) {
        return new ChatMessage("TEXT", content, null, null, null);
    }

    public static ChatMessage document(String title, String fileName, String downloadUrl) {
        return new ChatMessage("DOCUMENT", null, title, fileName, downloadUrl);
    }
}
