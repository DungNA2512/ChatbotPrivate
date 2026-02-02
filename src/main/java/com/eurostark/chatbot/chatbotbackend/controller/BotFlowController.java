package com.eurostark.chatbot.chatbotbackend.controller;

import com.eurostark.chatbot.chatbotbackend.dto.ChatMessage;
import com.eurostark.chatbot.chatbotbackend.service.BotFlowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/chatbot")
public class BotFlowController {
    private final BotFlowService botFlowService;

    public BotFlowController(BotFlowService botFlowService) {
        this.botFlowService = botFlowService;
    }

    @PostMapping("/send")
    public ResponseEntity<ChatMessage> sendMessage(
            @RequestBody Map<String, String> body) {

        String sessionId = body.get("sessionId");
        String message = body.getOrDefault("message", "");

        ChatMessage reply = botFlowService.handleUserMessage(sessionId, message);

        return ResponseEntity.ok(reply);
    }
}
