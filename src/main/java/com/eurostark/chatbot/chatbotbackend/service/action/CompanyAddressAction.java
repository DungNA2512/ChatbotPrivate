package com.eurostark.chatbot.chatbotbackend.service.action;

import org.springframework.stereotype.Component;
import com.eurostark.chatbot.chatbotbackend.entity.ChatContext;
import com.eurostark.chatbot.chatbotbackend.dto.ChatMessage;
import com.eurostark.chatbot.chatbotbackend.entity.ActionType;

@Component
public class CompanyAddressAction implements ActionHandler {

    @Override
    public ActionType supports() {
        return ActionType.SHOW_COMPANY_ADDRESS;
    }

    @Override
    public ChatMessage handle(ChatContext context) {
        return ChatMessage.text("""
                📍 Địa chỉ công ty:
                Tầng 10, Toà nhà ABC
                Quận 1, TP.HCM
                """);
    }
}
