package com.eurostark.chatbot.chatbotbackend.entity;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatSession {

    private String userId;
    private Long currentStepId;

    private ChatContext context = new ChatContext();

    private Instant lastActiveAt; // ⭐ thêm
}
