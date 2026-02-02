package com.eurostark.chatbot.chatbotbackend.service.action;

import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import com.eurostark.chatbot.chatbotbackend.dto.ChatMessage;
import com.eurostark.chatbot.chatbotbackend.entity.ActionType;
import com.eurostark.chatbot.chatbotbackend.entity.ChatContext;
import com.eurostark.chatbot.chatbotbackend.entity.ContextKey;
import com.eurostark.chatbot.chatbotbackend.exception.ChatBotException;
import com.eurostark.chatbot.chatbotbackend.entity.Document;
import com.eurostark.chatbot.chatbotbackend.repository.DocumentRepository;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@Transactional(readOnly = true)
public class SendDocumentActionHandler implements ActionHandler {

    private final DocumentRepository documentRepository;

    public SendDocumentActionHandler(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Override
    public ActionType supports() {
        return ActionType.SEND_DOCUMENT;
    }

    @Override
    public ChatMessage handle(ChatContext context) {

        String code = context.get(ContextKey.DOCUMENT_CODE, String.class);

        // 🔥 LOG KIỂM TRA ACTION CÓ ĐƯỢC EXECUTE HAY KHÔNG
        log.info("🔥 SEND_DOCUMENT 실행, code={}", code);

        if (code == null) {
            throw new ChatBotException("Không xác định được tài liệu.");
        }

        Document doc = documentRepository.findByCode(code)
                .orElseThrow(() -> new ChatBotException("Không tìm thấy tài liệu."));

        return ChatMessage.document(
                doc.getTitle(),
                doc.getFileName(),
                "http://localhost:8080/api/documents/" + doc.getId() + "/download");
    }
}