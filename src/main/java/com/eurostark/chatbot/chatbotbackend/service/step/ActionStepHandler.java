package com.eurostark.chatbot.chatbotbackend.service.step;

import com.eurostark.chatbot.chatbotbackend.entity.BotFlow;
import com.eurostark.chatbot.chatbotbackend.entity.ChatContext;
import com.eurostark.chatbot.chatbotbackend.entity.ChatSession;
import com.eurostark.chatbot.chatbotbackend.entity.ContextKey;
import com.eurostark.chatbot.chatbotbackend.entity.StepType;
import com.eurostark.chatbot.chatbotbackend.service.ActionExecutor;
import com.eurostark.chatbot.chatbotbackend.dto.ChatMessage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ActionStepHandler implements StepHandler {

    private final ActionExecutor actionExecutor;

    public ActionStepHandler(ActionExecutor actionExecutor) {
        this.actionExecutor = actionExecutor;
    }

    @Override
    public StepType supports() {
        return StepType.ACTION;
    }

    @Override
    public ChatMessage handle(ChatSession session, BotFlow step, String ignoredInput) {

        ChatContext context = session.getContext();

        // ⭐ ActionExecutor giờ trả ChatMessage
        ChatMessage message = actionExecutor.execute(step.getAction(), context);

        context.put(ContextKey.ACTION_EXECUTED, true);

        // ⚠️ rất quan trọng:
        // ACTION xong thì reset step để quay về root / menu
        session.setCurrentStepId(null);

        return message;
    }
}
