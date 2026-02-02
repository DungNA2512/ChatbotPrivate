package com.eurostark.chatbot.chatbotbackend.service;

import com.eurostark.chatbot.chatbotbackend.entity.Message;
import com.eurostark.chatbot.chatbotbackend.repository.MessageRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ChatService {
    private final MessageRepository messageRepository;

    public ChatService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message sendMessage(Message msg) {
        return messageRepository.save(msg);
    }

    public List<Message> getMessagesBySender(String sender) {
        return messageRepository.findAll().stream()
                .filter(m -> m.getSender().equals(sender))
                .toList();
    }

    public List<Message> getMessagesByReceiver(String receiver) {
        return messageRepository.findAll().stream()
                .filter(m -> m.getReceiver().equals(receiver))
                .toList();
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }
}
