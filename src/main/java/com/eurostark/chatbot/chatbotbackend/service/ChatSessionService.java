package com.eurostark.chatbot.chatbotbackend.service;

import org.springframework.stereotype.Service;
import com.eurostark.chatbot.chatbotbackend.entity.ChatSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatSessionService {
    private final Map<String, ChatSession> sessions = new ConcurrentHashMap<>();

    public ChatSession getOrCreate(String sessionId) {

        if (sessionId == null || sessionId.isBlank()) {
            throw new IllegalArgumentException("sessionId must not be null or blank");
        }

        return sessions.computeIfAbsent(sessionId, id -> {
            ChatSession s = new ChatSession();
            s.setUserId(id);
            return s;
        });
    }

    public void updateStep(String sessionId, Long stepId) {
        ChatSession session = getOrCreate(sessionId);
        session.setCurrentStepId(stepId);
    }

    public void reset(String sessionId) {
        sessions.remove(sessionId);
    }
}
