package com.eurostark.chatbot.chatbotbackend.service;

import com.eurostark.chatbot.chatbotbackend.dto.ChatMessage;
import com.eurostark.chatbot.chatbotbackend.entity.BotFlow;
import com.eurostark.chatbot.chatbotbackend.entity.ChatSession;
import com.eurostark.chatbot.chatbotbackend.entity.ContextKey;
import com.eurostark.chatbot.chatbotbackend.entity.StepType;
import com.eurostark.chatbot.chatbotbackend.repository.BotFlowRepository;
import com.eurostark.chatbot.chatbotbackend.service.step.StepHandler;
import com.eurostark.chatbot.chatbotbackend.service.step.StepHandlerFactory;

import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class BotFlowService {
    private final BotFlowRepository repo;
    private final ChatSessionService chatSessionService;
    private final StepHandlerFactory stepHandlerFactory;
    private static final Duration SESSION_TIMEOUT = Duration.ofMinutes(30);

    public BotFlowService(BotFlowRepository repo, ChatSessionService chatSessionService,
            StepHandlerFactory stepHandlerFactory) {
        this.repo = repo;
        this.chatSessionService = chatSessionService;
        this.stepHandlerFactory = stepHandlerFactory;
    }

    // // ================= HELPERS =================
    // private boolean matchesTrigger(String triggerText, String inputLower) {
    // if (!StringUtils.hasText(triggerText))
    // return false;
    // String[] tokens = triggerText.toLowerCase(Locale.ROOT).split(",");
    // for (String tok : tokens) {
    // tok = tok.trim();
    // if (tok.isEmpty())
    // continue;
    // // If token appears anywhere in input
    // if (inputLower.contains(tok))
    // return true;
    // }
    // return false;
    // }

    // private String buildResponseForStep(BotFlow step) {
    // StringBuilder resp = new StringBuilder();
    // resp.append(step.getBotMessage());

    // // List children (menu) if any
    // List<BotFlow> children =
    // repo.findByParentStepIdOrderByOrderIndexAsc(step.getId());
    // if (!children.isEmpty()) {
    // resp.append("\n\nBạn có thể chọn:\n");
    // for (int i = 0; i < children.size(); i++) {
    // BotFlow c = children.get(i);
    // // Show simple title: first token of triggerText
    // String title = c.getTriggerText();
    // if (title != null && title.contains(","))
    // title = title.split(",")[0];
    // resp.append("\n").append(i + 1).append(". ").append(title);
    // }
    // }
    // return resp.toString();
    // }

    // ================= ENTRY POINT =================
    public ChatMessage handleUserMessage(String sessionId, String input) {

        ChatSession session = chatSessionService.getOrCreate(sessionId);
        Instant now = Instant.now();

        // ⏱ timeout
        if (session.getLastActiveAt() != null &&
                Duration.between(session.getLastActiveAt(), now)
                        .compareTo(SESSION_TIMEOUT) > 0) {
            resetSession(session);
        }

        session.setLastActiveAt(now);
        session.getContext().put(ContextKey.SESSION_ID, sessionId);

        String userInput = input == null ? "" : input.trim();
        ChatMessage response = null;

        // 🔁 STATE MACHINE LOOP
        while (true) {

            // 🟢 chưa có step → show root
            if (session.getCurrentStepId() == null) {
                response = ChatMessage.text(showRoot(session));
                break;
            }

            BotFlow step = repo.findById(session.getCurrentStepId()).orElse(null);

            if (step == null) {
                session.setCurrentStepId(null);
                response = ChatMessage.text("❌ Lỗi phiên làm việc: bước hiện tại không tồn tại. Đã reset phiên.");
                break;
            }

            StepHandler handler = stepHandlerFactory.getHandler(step.getStepType());

            response = handler.handle(session, step, userInput);
            // 🔥 NẾU sau handle mà currentStep là ACTION → chạy tiếp
            Long nextStepId = session.getCurrentStepId();
            if (nextStepId != null) {
                BotFlow nextStep = repo.findById(nextStepId).orElse(null);

                if (nextStep != null && nextStep.getStepType() == StepType.ACTION) {
                    StepHandler actionHandler = stepHandlerFactory.getHandler(StepType.ACTION);

                    response = actionHandler.handle(session, nextStep, userInput);

                    session.setCurrentStepId(null); // reset
                }
            }

            break;
        }

        return response;
    }

    private void resetSession(ChatSession session) {
        session.setCurrentStepId(null);
        session.getContext().clear();
    }
    // private String handleSelection(ChatSession session, int choice) {
    // if (choice <= 0)
    // return showRoot(session);

    // Long parentId = session.getCurrentStepId();
    // List<BotFlow> options =
    // repo.findByParentStepIdOrderByOrderIndexAsc(parentId);

    // if (options.isEmpty() || choice > options.size()) {
    // return "Lựa chọn không hợp lệ.";
    // }

    // BotFlow selected = options.get(choice - 1);
    // session.setCurrentStepId(selected.getId());

    // return buildResponseForStep(selected);
    // }

    // private String handleKeyword(ChatSession session, String input) {
    // Long parentId = session.getCurrentStepId();

    // List<BotFlow> candidates = repo.findAll().stream()
    // .filter(f -> Objects.equals(f.getParentStepId(), parentId))
    // .collect(Collectors.toList());

    // for (BotFlow flow : candidates) {
    // if (matchesTrigger(flow.getTriggerText(), input)) {
    // if (flow.getStepType() == StepType.INPUT) {
    // String error = validateInput(session, flow, input);
    // if (error != null) {
    // return error;
    // }
    // }
    // session.setCurrentStepId(flow.getId());
    // return buildResponseForStep(flow);
    // }
    // }

    // return "Tôi chưa hiểu. Vui lòng chọn lại.";
    // }

    // ================= ROOT =================
    private String showRoot(ChatSession session) {

        BotFlow root = repo.findByParentStepIdOrderByOrderIndexAsc(null)
                .stream()
                .findFirst()
                .orElse(null);

        if (root == null) {
            return "Chưa cấu hình chatbot.";
        }

        session.setCurrentStepId(root.getId());

        StringBuilder resp = new StringBuilder();
        resp.append(root.getBotMessage());

        List<BotFlow> children = repo.findByParentStepIdOrderByOrderIndexAsc(root.getId());

        if (!children.isEmpty()) {
            resp.append("\n\nBạn có thể chọn:");
            for (int i = 0; i < children.size(); i++) {
                BotFlow c = children.get(i);
                String title = c.getTriggerText();
                if (title != null && title.contains(",")) {
                    title = title.split(",")[0];
                }
                resp.append("\n").append(i + 1).append(". ").append(title);
            }
        }

        return resp.toString();
    }

    // // ================= INPUT =================
    // private String validateInput(ChatSession session, BotFlow step, String input)
    // {
    // // validate data
    // if ("DATE".equalsIgnoreCase(step.getValidation())) {
    // if (!input.matches("\\d{4}-\\d{2}-\\d{2}")) {
    // return "Ngày không hợp lệ. Định dạng yyyy-MM-dd";
    // }
    // }

    // // tìm CONFIRM step
    // BotFlow confirmStep = repo.findAll().stream()
    // .filter(f -> Objects.equals(f.getParentStepId(), step.getId()))
    // .filter(f -> f.getStepType() == StepType.CONFIRM)
    // .findFirst()
    // .orElse(null);

    // if (confirmStep == null) {
    // return "❌ Lỗi cấu hình flow: thiếu bước CONFIRM";
    // }

    // session.setCurrentStepId(confirmStep.getId());

    // return String.format(confirmStep.getBotMessage(), input);
    // }

    // private void handleAction(BotFlow step, ChatSession session) {
    // if (step.getAction() == null)
    // return;

    // switch (step.getAction()) {
    // case "SAVE_LEAVE":
    // System.out.println("Saving leave request for session " +
    // session.getUserId());
    // break;
    // case "CREATE_TICKET":
    // System.out.println("Creating IT ticket");
    // break;
    // }

    // }

    // private String handleInputStep(ChatSession session, BotFlow currentStep,
    // String userInput) {
    // log.debug("[INPUT] stepId={}, input={}", currentStep.getId(), userInput);

    // String inputKey = currentStep.getInputKey();

    // if (inputKey != null && !inputKey.isBlank()) {
    // session.getContext().put(inputKey, userInput);
    // log.info(
    // "[CONTEXT] saved {}={} for session={}",
    // inputKey,
    // userInput,
    // session.getUserId());
    // }

    // List<BotFlow> nextSteps =
    // repo.findByParentStepIdOrderByOrderIndexAsc(currentStep.getId());

    // if (nextSteps.isEmpty()) {
    // session.setCurrentStepId(null);
    // return "Cảm ơn bạn. Yêu cầu đã được ghi nhận.";
    // }

    // BotFlow next = nextSteps.get(0);
    // session.setCurrentStepId(next.getId());

    // return buildResponseForStep(next);
    // }

    // private String buildResponseForStep(BotFlow step, ChatSession session) {

    // if (step.getStepType() == StepType.CONFIRM) {
    // String date = (String) session.getContext().get("leaveDate");
    // return String.format(step.getBotMessage(), date);
    // }

    // return step.getBotMessage();
    // }

    // // ================= MENU =================
    // private String handleMenu(ChatSession session, String input) {
    // int index = Integer.parseInt(input);

    // List<BotFlow> children = repo.findByParentStepIdOrderByOrderIndexAsc(
    // session.getCurrentStepId());

    // BotFlow selected = children.get(index - 1);
    // session.setCurrentStepId(selected.getId());

    // return selected.getBotMessage();
    // }

    // // ================= ACTION =================
    // private String executeAction(BotFlow step, ChatSession session) {
    // String action = step.getAction();

    // log.debug(
    // "stepType = ACTION {}, context = {}",
    // action,
    // session.getContext());

    // // 1️⃣ Không có action → kết thúc luôn
    // if (action == null || action.isBlank()) {
    // session.setCurrentStepId(null);
    // return step.getBotMessage() != null
    // ? step.getBotMessage()
    // : "Hoàn tất.";
    // }

    // // 2️⃣ Thực thi action
    // actionExecutor.execute(action, session.getContext());

    // // 3️⃣ Kết thúc session sau ACTION
    // session.setCurrentStepId(null);

    // // 4️⃣ Trả message
    // return step.getBotMessage() != null
    // ? step.getBotMessage()
    // : "Hoàn tất.";
    // }

    // private BotFlow getCurrentStep(ChatSession session) {
    // return repo
    // .findById(session.getCurrentStepId())
    // .orElseThrow();
    // }
}
