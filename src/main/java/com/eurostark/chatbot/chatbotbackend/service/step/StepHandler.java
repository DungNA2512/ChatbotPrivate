package com.eurostark.chatbot.chatbotbackend.service.step;

import com.eurostark.chatbot.chatbotbackend.dto.ChatMessage;
import com.eurostark.chatbot.chatbotbackend.entity.BotFlow;
import com.eurostark.chatbot.chatbotbackend.entity.ChatSession;
import com.eurostark.chatbot.chatbotbackend.entity.StepType;

public interface StepHandler {

    StepType supports();

    ChatMessage handle(ChatSession session, BotFlow currentStep, String userInput);

}
