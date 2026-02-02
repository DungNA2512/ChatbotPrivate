package com.eurostark.chatbot.chatbotbackend.service.step;

import com.eurostark.chatbot.chatbotbackend.repository.BotFlowRepository;
import com.eurostark.chatbot.chatbotbackend.entity.ChatSession;
import com.eurostark.chatbot.chatbotbackend.entity.ContextKey;
import com.eurostark.chatbot.chatbotbackend.entity.StepType;
import com.eurostark.chatbot.chatbotbackend.dto.ChatMessage;
import com.eurostark.chatbot.chatbotbackend.entity.BotFlow;

import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Component
@Slf4j
public class InputStepHandler implements StepHandler {

    private final BotFlowRepository repo;

    public InputStepHandler(BotFlowRepository repo) {
        this.repo = repo;
    }

    @Override
    public StepType supports() {
        return StepType.INPUT;
    }

    @Override
    public ChatMessage handle(ChatSession session, BotFlow step, String input) {
        log.debug("[INPUT] stepId={}, input={}", step.getId(), input);

        // validation
        String error = validate(step, input);
        if (error != null) {
            return ChatMessage.text(error);
        }

        // save context
        if ("DATE".equals(step.getValidation())) {
            session.getContext().put(
                    ContextKey.LEAVE_DATE,
                    input);
            log.info("[CONTEXT] save leaveDate={}", input);
        }

        List<BotFlow> next = repo.findByParentStepIdOrderByOrderIndexAsc(step.getId());

        if (next.isEmpty()) {
            session.setCurrentStepId(null);
            return ChatMessage.text("hoàn tất.");
        }

        session.setCurrentStepId(next.get(0).getId());
        return ChatMessage.text(next.get(0).getBotMessage());
    }

    private String validate(BotFlow step, String input) {
        String rule = step.getValidation();
        if ("DATE".equals(rule)) {
            try {
                java.time.LocalDate.parse(input);
            } catch (Exception e) {
                log.warn("[INPUT] invalid date format: {}", input);
                return "Định dạng ngày không hợp lệ. Vui lòng nhập lại (yyyy-MM-dd).";
            }
        }
        return null;
    }
}