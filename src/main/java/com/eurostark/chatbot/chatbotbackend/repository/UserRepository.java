package com.eurostark.chatbot.chatbotbackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eurostark.chatbot.chatbotbackend.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByExternalId(String externalId);
}