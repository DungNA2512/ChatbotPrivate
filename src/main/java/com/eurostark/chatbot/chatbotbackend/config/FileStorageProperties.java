package com.eurostark.chatbot.chatbotbackend.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class FileStorageProperties {

    @Value("${app.file.upload-dir}")
    private String uploadDir;
}