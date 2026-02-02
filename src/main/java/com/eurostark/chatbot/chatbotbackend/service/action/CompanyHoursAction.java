package com.eurostark.chatbot.chatbotbackend.service.action;

import org.springframework.stereotype.Component;
import com.eurostark.chatbot.chatbotbackend.entity.ChatContext;
import com.eurostark.chatbot.chatbotbackend.dto.ChatMessage;
import com.eurostark.chatbot.chatbotbackend.entity.ActionType;

@Component
public class CompanyHoursAction implements ActionHandler {

    @Override
    public ActionType supports() {
        return ActionType.SHOW_COMPANY_HOURS;
    }

    @Override
    public ChatMessage handle(ChatContext context) {
        return ChatMessage.text("""
                🕒 Giờ làm việc:
                Thứ Hai - Thứ Sáu: 8:00 AM - 6:00 PM
                Thứ Bảy - Chủ Nhật: Nghỉ
                """);
    }

}
