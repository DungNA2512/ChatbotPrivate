package com.eurostark.chatbot.chatbotbackend.service.step;

import org.springframework.stereotype.Component;
import com.eurostark.chatbot.chatbotbackend.entity.StepType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class StepHandlerFactory {
    private final Map<StepType, StepHandler> handlerMap;

    public StepHandlerFactory(List<StepHandler> handlers) {
        this.handlerMap = handlers.stream()
                .collect(Collectors.toMap(
                        StepHandler::supports,
                        h -> h));
    }

    public StepHandler getHandler(StepType type) {
        StepHandler handler = handlerMap.get(type);
        if (handler == null) {
            throw new IllegalArgumentException("No handler found for step type: " + type);
        }
        return handler;
    }
}
