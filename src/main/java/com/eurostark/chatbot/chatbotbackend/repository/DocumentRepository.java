package com.eurostark.chatbot.chatbotbackend.repository;

import com.eurostark.chatbot.chatbotbackend.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    Optional<Document> findByCode(String code);
}
