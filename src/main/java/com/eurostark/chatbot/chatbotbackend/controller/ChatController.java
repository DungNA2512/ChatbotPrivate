package com.eurostark.chatbot.chatbotbackend.controller;

import com.eurostark.chatbot.chatbotbackend.dto.ChatMessage;
import com.eurostark.chatbot.chatbotbackend.entity.Message;
import com.eurostark.chatbot.chatbotbackend.service.ChatService;
import com.eurostark.chatbot.chatbotbackend.service.BotFlowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatService chatService;
    private final BotFlowService botFlowService;

    public ChatController(ChatService chatService, BotFlowService botFlowService) {
        this.chatService = chatService;
        this.botFlowService = botFlowService;
    }

    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> chatWithBot(
            @RequestBody Map<String, String> request) {

        String sessionId = request.get("sessionId");
        String userMessage = request.get("message");

        ChatMessage botReply = botFlowService.handleUserMessage(sessionId, userMessage);
        Map<String, String> response = new HashMap<>();
        response.put("reply", botReply.getContent());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/save")
    public ResponseEntity<Message> sendMessage(@RequestBody Message msg) {
        Message sent = chatService.sendMessage(msg);
        return ResponseEntity.ok(sent);
    }

    @GetMapping("/sender/{sender}")
    public List<Message> getMessagesBySender(@PathVariable String sender) {
        return chatService.getMessagesBySender(sender);
    }

    @GetMapping("/receiver/{receiver}")
    public List<Message> getMessagesByReceiver(@PathVariable String receiver) {
        return chatService.getMessagesByReceiver(receiver);
    }

    @GetMapping("/all")
    public List<Message> getAllMessages() {
        return chatService.getAllMessages();
    }
}
