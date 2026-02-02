package com.eurostark.chatbot.chatbotbackend.service.action;

import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import com.eurostark.chatbot.chatbotbackend.dto.ChatMessage;
import com.eurostark.chatbot.chatbotbackend.entity.ActionType;
import com.eurostark.chatbot.chatbotbackend.entity.ChatContext;
import com.eurostark.chatbot.chatbotbackend.entity.ContextKey;
import com.eurostark.chatbot.chatbotbackend.service.LeaveRequestService;
import com.eurostark.chatbot.chatbotbackend.exception.ChatBotException;

@Component
@Slf4j
public class SaveLeaveActionHandler implements ActionHandler {

    private final LeaveRequestService leaveRequestService;

    public SaveLeaveActionHandler(LeaveRequestService leaveRequestService) {
        this.leaveRequestService = leaveRequestService;
    }

    @Override
    public ActionType supports() {
        return ActionType.SAVE_LEAVE;
    }

    @Override
    public ChatMessage handle(ChatContext context) {

        String sessionId = context.get(ContextKey.SESSION_ID, String.class);
        String leaveDate = context.get(ContextKey.LEAVE_DATE, String.class);

        if (sessionId == null || leaveDate == null) {
            log.error("[SAVE_LEAVE] missing data. sessionId={}, leaveDate={}", sessionId, leaveDate);
            throw new ChatBotException("Thiếu thông tin để lưu yêu cầu nghỉ phép.");
        }

        leaveRequestService.saveLeave(sessionId, leaveDate);

        log.info("[SAVE_LEAVE] success. session={}, date={}", sessionId, leaveDate);

        return ChatMessage.text("✅ Đã lưu yêu cầu nghỉ phép.");
    }
}