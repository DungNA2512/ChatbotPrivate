package com.eurostark.chatbot.chatbotbackend;

import com.eurostark.chatbot.chatbotbackend.entity.BotFlow;
import com.eurostark.chatbot.chatbotbackend.repository.BotFlowRepository;
import com.eurostark.chatbot.chatbotbackend.entity.StepType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ChatbotBackendApplication {

      public static void main(String[] args) {
            SpringApplication.run(ChatbotBackendApplication.class, args);
      }

      @Bean
      CommandLineRunner init(BotFlowRepository repo) {
            return args -> {
                  if (repo.count() > 0) {
                        return; // đã có dữ liệu → không seed nữa
                  }

                  // ===== ROOT MENU =====
                  BotFlow root = repo.save(
                              BotFlow.builder()
                                          .triggerText("hi,hello,xin chào,chào")
                                          .botMessage("Chào bạn! Bạn muốn làm gì?")
                                          .stepType(StepType.MENU)
                                          .parentStepId(null)
                                          .orderIndex(0)
                                          .build());

                  // ===== LEAVE REQUEST =====
                  BotFlow leaveInput = repo.save(
                              BotFlow.builder()
                                          .triggerText("xin nghỉ phép,nghỉ phép")
                                          .botMessage("Bạn muốn xin nghỉ vào ngày nào? (yyyy-MM-dd)")
                                          .stepType(StepType.INPUT)
                                          .validation("DATE")
                                          .inputKey("leaveDate")
                                          .action(null)
                                          .parentStepId(root.getId())
                                          .orderIndex(1)
                                          .build());

                  BotFlow leaveConfirm = repo.save(
                              BotFlow.builder()
                                          .botMessage("Bạn xác nhận xin nghỉ vào ngày %s? (1: Xác nhận/2: Không)")
                                          .stepType(StepType.CONFIRM)
                                          .action(null)
                                          .parentStepId(leaveInput.getId())
                                          .orderIndex(1)
                                          .build());

                  // 4️⃣ ACTION
                  repo.save(
                              BotFlow.builder()
                                          .parentStepId(leaveConfirm.getId())
                                          .triggerText("1,yes,ok,đồng ý")
                                          .stepType(StepType.ACTION)
                                          .action("SAVE_LEAVE")
                                          .botMessage("✅ Đã ghi nhận đơn xin nghỉ. Cảm ơn bạn!")
                                          .orderIndex(1)
                                          .build());

                  // ===== IT SUPPORT =====
                  BotFlow itInput = repo.save(
                              BotFlow.builder()
                                          .triggerText("hỗ trợ it,it,lỗi,máy tính")
                                          .botMessage("Bạn gặp lỗi gì? Hãy mô tả ngắn gọn.")
                                          .stepType(StepType.INPUT)
                                          .action(null)
                                          .parentStepId(root.getId())
                                          .orderIndex(2)
                                          .build());

                  BotFlow itConfirm = repo.save(
                              BotFlow.builder()
                                          .botMessage("Bạn xác nhận gửi yêu cầu IT với nội dung: \"%s\" ? (yes/no)")
                                          .stepType(StepType.CONFIRM)
                                          .action(null)
                                          .parentStepId(itInput.getId())
                                          .orderIndex(1)
                                          .build());

                  repo.save(
                              BotFlow.builder()
                                          .botMessage("Yêu cầu IT đã được ghi nhận. Cảm ơn bạn!")
                                          .stepType(StepType.ACTION)
                                          .action("CREATE_TICKET")
                                          .parentStepId(itConfirm.getId())
                                          .orderIndex(1)
                                          .build());

                  // ===== THÔNG TIN CÔNG TY =====
                  BotFlow companyInfo = repo.save(
                              BotFlow.builder()
                                          .triggerText("thông tin công ty,địa chỉ công ty,company info")
                                          .botMessage(
                                                      "📍 Địa chỉ công ty:\n" +
                                                                  "Tầng 10, Toà nhà ABC\n" +
                                                                  "Quận 1, TP.HCM\n\n" +
                                                                  "☎ Hotline: 0123 456 789\n" +
                                                                  "🌐 Website: eurostark.com")
                                          .stepType(StepType.ACTION)
                                          .action("SHOW_COMPANY_INFO")
                                          .parentStepId(root.getId())
                                          .orderIndex(3)
                                          .build());

                  // ===== DOCUMENT MENU =====
                  BotFlow documentMenu = repo.save(
                              BotFlow.builder()
                                          .triggerText("tài liệu,policy")
                                          .botMessage("Bạn muốn xem tài liệu nào?\n1. Form nghỉ phép\n2. Quyết định và quy chế quản lý chi tiêu nội bộ\n3. Form xin nghỉ việc")
                                          .stepType(StepType.MENU)
                                          .parentStepId(root.getId())
                                          .orderIndex(4)
                                          .build());

                  repo.save(
                              BotFlow.builder()
                                          .triggerText("1,nghỉ phép")
                                          .stepType(StepType.ACTION)
                                          .action("SEND_DOCUMENT:LEAVE_TEMPORARY")
                                          .botMessage("📄 Đây là tài liệu bạn yêu cầu:")
                                          .parentStepId(documentMenu.getId())
                                          .orderIndex(1)
                                          .build());
                  repo.save(
                              BotFlow.builder()
                                          .triggerText("2,nội quy")
                                          .stepType(StepType.ACTION)
                                          .action("SEND_DOCUMENT:EXPENSE_MANAGEMENT_POLICY")
                                          .botMessage("📄 Đây là tài liệu bạn yêu cầu:")
                                          .parentStepId(documentMenu.getId())
                                          .orderIndex(2)
                                          .build());
                  repo.save(
                              BotFlow.builder()
                                          .triggerText("3,xin nghỉ")
                                          .stepType(StepType.ACTION)
                                          .action("SEND_DOCUMENT:LEAVE_FORM")
                                          .botMessage("📄 Đây là tài liệu bạn yêu cầu:")
                                          .parentStepId(documentMenu.getId())
                                          .orderIndex(2)
                                          .build());
            };
      }
}
