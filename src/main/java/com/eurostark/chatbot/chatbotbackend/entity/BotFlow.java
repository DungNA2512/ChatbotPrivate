package com.eurostark.chatbot.chatbotbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "bot_flows")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BotFlow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Comma-separated keywords, e.g. "hi,hello,xin chào"
    @Column(name = "trigger_text", nullable = false)
    @Builder.Default
    private String triggerText = "*";

    @Column(name = "bot_message", columnDefinition = "TEXT")
    private String botMessage;

    @Column(name = "parent_step_id")
    private Long parentStepId; // null means root step

    @Column(name = "input_key")
    private String inputKey; // key to store input value in context

    @Column(name = "order_index")
    private Integer orderIndex;

    @Enumerated(EnumType.STRING)
    @Column(name = "step_type", nullable = false)
    private StepType stepType; // MENU, INPUT, CONFIRM, END

    private String validation; // regex / DATE / NUMBER

    private String action; // SAVE_LEAVE, CREATE_TICKET
}
