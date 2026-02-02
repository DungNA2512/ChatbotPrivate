package com.eurostark.chatbot.chatbotbackend.service.action;

import com.eurostark.chatbot.chatbotbackend.dto.ChatMessage;
import com.eurostark.chatbot.chatbotbackend.entity.ActionType;
import com.eurostark.chatbot.chatbotbackend.entity.ChatContext;
import org.springframework.stereotype.Component;

@Component
public class CompanyContactAction implements ActionHandler {
    @Override
    public ActionType supports() {
        return ActionType.SHOW_COMPANY_CONTACT;
    }

    @Override
    public ChatMessage handle(ChatContext context) {
        return ChatMessage.text("""
                ☎ Hotline: 0123 456 789
                📞 Điện thoại: 0987 654 321
                📧 Email: contact@company.com
                """);
    }
}
