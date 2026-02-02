package com.eurostark.chatbot.chatbotbackend.service.action;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.eurostark.chatbot.chatbotbackend.entity.ActionType;

@Component
public class ActionHandlerFactory {

    private final Map<ActionType, ActionHandler> handlers;

    public ActionHandlerFactory(List<ActionHandler> handlerList) {
        this.handlers = handlerList.stream()
                .collect(Collectors.toMap(
                        ActionHandler::supports,
                        h -> h));
    }

    public ActionHandler get(ActionType type) {
        return handlers.get(type);
    }
}