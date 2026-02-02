package com.eurostark.chatbot.chatbotbackend.entity;

import java.util.EnumMap;
import java.util.Map;

public class ChatContext {

    private final Map<ContextKey, Object> data = new EnumMap<>(ContextKey.class);

    public void put(ContextKey key, Object value) {
        data.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(ContextKey key, Class<T> type) {
        Object value = data.get(key);
        if (value == null) {
            return null;
        }

        if (!type.isInstance(value)) {
            throw new IllegalStateException(
                    "Context key " + key +
                            " expected " + type.getSimpleName() +
                            " but was " + value.getClass().getSimpleName());
        }

        return type.cast(value);
    }

    public boolean has(ContextKey key) {
        return data.containsKey(key);
    }

    public void clear() {
        data.clear();
    }

}