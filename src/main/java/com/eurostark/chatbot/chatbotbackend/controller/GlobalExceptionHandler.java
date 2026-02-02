package com.eurostark.chatbot.chatbotbackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.eurostark.chatbot.chatbotbackend.exception.ChatBotException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ChatBotException.class)
    public String handleChatBotException(ChatBotException ex) {
        log.warn("[CHATBOT ERROR] {}", ex.getMessage());
        return "❌ " + ex.getMessage();
    }

    @ExceptionHandler(Exception.class)
    public String handleUnexpected(Exception ex) {
        log.error("[UNEXPECTED ERROR]", ex);
        return "❌ Có lỗi hệ thống xảy ra. Vui lòng thử lại sau.";
    }
}