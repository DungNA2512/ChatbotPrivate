package com.eurostark.chatbot.chatbotbackend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.eurostark.chatbot.chatbotbackend.dto.ChatMessage;
import com.eurostark.chatbot.chatbotbackend.entity.ActionType;
import com.eurostark.chatbot.chatbotbackend.entity.ChatContext;
import com.eurostark.chatbot.chatbotbackend.entity.ContextKey;
import com.eurostark.chatbot.chatbotbackend.exception.ChatBotException;
import com.eurostark.chatbot.chatbotbackend.service.action.ActionHandler;
import com.eurostark.chatbot.chatbotbackend.service.action.ActionHandlerFactory;

@Component
public class ActionExecutor {

    private static final Logger log = LoggerFactory.getLogger(ActionExecutor.class);

    private final ActionHandlerFactory factory;

    public ActionExecutor(ActionHandlerFactory factory) {
        this.factory = factory;
    }

    // ⚠️ PHẢI public
    public ChatMessage execute(String rawAction, ChatContext context) {

        if (rawAction == null || rawAction.isBlank()) {
            return ChatMessage.text("Không có hành động để thực thi.");
        }

        String[] parts = rawAction.split(":", 2);
        String actionName = parts[0];
        String param = parts.length > 1 ? parts[1] : null;

        ActionType actionType = ActionType.from(actionName);

        if (actionType == null) {
            log.warn("Unknown action: {}", rawAction);
            return ChatMessage.text("Không tìm thấy hành động phù hợp.");
        }

        // ⭐ VALIDATE THEO TỪNG ACTION
        switch (actionType) {
            case SEND_DOCUMENT -> {
                if (param == null || param.isBlank()) {
                    throw new ChatBotException("Không xác định được tài liệu.");
                }
                context.put(ContextKey.DOCUMENT_CODE, param);
            }
            default -> {
                // các action khác nếu cần param thì validate sau
            }
        }

        ActionHandler handler = factory.get(actionType);

        if (handler == null) {
            log.error("No handler for action {}", actionType);
            return ChatMessage.text("Hành động chưa được hỗ trợ.");
        }

        return handler.handle(context);
    }
}