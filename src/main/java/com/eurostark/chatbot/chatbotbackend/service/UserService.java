package com.eurostark.chatbot.chatbotbackend.service;

import org.springframework.stereotype.Service;
import com.eurostark.chatbot.chatbotbackend.entity.User;
import com.eurostark.chatbot.chatbotbackend.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public User getOrCreate(String externalId) {
        return repo.findByExternalId(externalId)
                .orElseGet(() -> {
                    User u = new User();
                    u.setExternalId(externalId);
                    return repo.save(u);
                });
    }
}
