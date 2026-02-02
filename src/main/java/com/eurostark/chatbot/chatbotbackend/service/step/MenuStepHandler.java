package com.eurostark.chatbot.chatbotbackend.service.step;

import org.springframework.stereotype.Component;

import com.eurostark.chatbot.chatbotbackend.repository.BotFlowRepository;
import com.eurostark.chatbot.chatbotbackend.dto.ChatMessage;
import com.eurostark.chatbot.chatbotbackend.entity.BotFlow;
import com.eurostark.chatbot.chatbotbackend.entity.ChatSession;
import com.eurostark.chatbot.chatbotbackend.entity.StepType;

import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Component
@Slf4j
public class MenuStepHandler implements StepHandler {

    private final BotFlowRepository repo;

    public MenuStepHandler(
            BotFlowRepository repo) {
        this.repo = repo;
    }

    @Override
    public StepType supports() {
        return StepType.MENU;
    }

    @Override
    public ChatMessage handle(ChatSession session, BotFlow step, String input) {

        log.debug("[MENU] input={}", input);

        if (!input.matches("\\d+")) {
            return ChatMessage.text("Vui lòng chọn số.");
        }

        int index = Integer.parseInt(input) - 1;

        List<BotFlow> children = repo.findByParentStepIdOrderByOrderIndexAsc(step.getId());

        if (index < 0 || index >= children.size()) {
            return ChatMessage.text("Lựa chọn không hợp lệ.");
        }

        BotFlow next = children.get(index);
        session.setCurrentStepId(next.getId());

        return ChatMessage.text(next.getBotMessage());
    }
}