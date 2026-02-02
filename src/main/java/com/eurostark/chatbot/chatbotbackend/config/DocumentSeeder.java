package com.eurostark.chatbot.chatbotbackend.config;

import com.eurostark.chatbot.chatbotbackend.entity.Document;
import com.eurostark.chatbot.chatbotbackend.repository.DocumentRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Component
@Transactional
public class DocumentSeeder implements CommandLineRunner {

    private final DocumentRepository repo;

    public DocumentSeeder(DocumentRepository repo) {
        this.repo = repo;
    }

    @Override
    public void run(String... args) throws Exception {

        // Danh sách tài liệu seed
        List<DocumentSeed> documents = List.of(
                new DocumentSeed(
                        "LEAVE_TEMPORARY",
                        "Đơn xin nghỉ phép",
                        "application-for-leave.doc",
                        "application/msword"),
                new DocumentSeed(
                        "EXPENSE_MANAGEMENT_POLICY",
                        "Quyết định và Quy chế quản lý chi tiêu nội bộ",
                        "quet-dinh-va-quy-che-quan-ly-chi-tieu-noi-bo.pdf",
                        "application/pdf"),
                new DocumentSeed(
                        "LEAVE_FORM",
                        "Biểu mẫu xin nghỉ phép",
                        "leave-form.docx",
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"));

        for (DocumentSeed seed : documents) {

            if (repo.findByCode(seed.code()).isPresent()) {
                System.out.println("⚠️ Skip: " + seed.code());
                continue;
            }

            Path path = Paths.get("uploads/documents/" + seed.fileName());

            if (!Files.exists(path)) {
                System.err.println("❌ File not found: " + path.toAbsolutePath());
                continue;
            }

            byte[] data = Files.readAllBytes(path);

            Document doc = new Document();
            doc.setCode(seed.code());
            doc.setTitle(seed.title());
            doc.setFileName(seed.fileName());
            doc.setContentType(seed.contentType());
            doc.setData(data);

            repo.save(doc);
            System.out.println("✅ Seeded: " + seed.code());
        }
    }

    // Record helper class (Java 16+)
    private record DocumentSeed(String code, String title, String fileName, String contentType) {
    }
}