package com.eurostark.chatbot.chatbotbackend.service.action;

import com.eurostark.chatbot.chatbotbackend.dto.ChatMessage;

import com.eurostark.chatbot.chatbotbackend.entity.ActionType;
import com.eurostark.chatbot.chatbotbackend.entity.ChatContext;

public interface ActionHandler {

    ActionType supports();

    ChatMessage handle(ChatContext context);
}