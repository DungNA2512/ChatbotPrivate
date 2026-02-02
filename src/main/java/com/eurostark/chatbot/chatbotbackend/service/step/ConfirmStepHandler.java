package com.eurostark.chatbot.chatbotbackend.service.step;

import com.eurostark.chatbot.chatbotbackend.dto.ChatMessage;
import com.eurostark.chatbot.chatbotbackend.entity.BotFlow;
import com.eurostark.chatbot.chatbotbackend.entity.ChatSession;
import com.eurostark.chatbot.chatbotbackend.entity.StepType;
import com.eurostark.chatbot.chatbotbackend.repository.BotFlowRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class ConfirmStepHandler implements StepHandler {

    private static final Set<String> YES_INPUTS = Set.of("yes", "y", "1", "ok", "đồng ý");

    private static final Set<String> NO_INPUTS = Set.of("no", "n", "2", "không");

    private final BotFlowRepository repo;

    public ConfirmStepHandler(BotFlowRepository repo) {
        this.repo = repo;
    }

    @Override
    public StepType supports() {
        return StepType.CONFIRM;
    }

    @Override
    public ChatMessage handle(ChatSession session, BotFlow step, String input) {

        String normalized = normalize(input);

        log.debug(
                "[CONFIRM] stepId={}, input={}, session={}",
                step.getId(),
                normalized,
                session.getUserId());

        // ❌ từ chối
        if (NO_INPUTS.contains(normalized)) {
            session.setCurrentStepId(null);
            session.getContext().clear();
            return ChatMessage.text("❌ Đã huỷ yêu cầu.");
        }

        // ❌ không hợp lệ
        if (!YES_INPUTS.contains(normalized)) {
            return ChatMessage.text(step.getBotMessage());
        }

        // ✅ đồng ý
        List<BotFlow> nextSteps = repo.findByParentStepIdOrderByOrderIndexAsc(step.getId());

        if (nextSteps.isEmpty()) {
            session.setCurrentStepId(null);
            return ChatMessage.text("Hoàn tất.");
        }

        if (nextSteps.size() > 1) {
            log.warn(
                    "[CONFIRM] stepId={} has {} next steps, expected 1",
                    step.getId(),
                    nextSteps.size());
        }

        BotFlow next = nextSteps.get(0);
        session.setCurrentStepId(next.getId());

        return ChatMessage.text(next.getBotMessage());
    }

    private String normalize(String input) {
        return input == null ? "" : input.trim().toLowerCase();
    }
}
