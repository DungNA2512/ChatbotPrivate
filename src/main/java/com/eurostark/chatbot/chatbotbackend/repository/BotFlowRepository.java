package com.eurostark.chatbot.chatbotbackend.repository;

import com.eurostark.chatbot.chatbotbackend.entity.BotFlow;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface BotFlowRepository extends JpaRepository<BotFlow, Long> {
    List<BotFlow> findByParentStepIdOrderByOrderIndexAsc(Long parentStepId);

    BotFlow findFirstByParentStepId(Long parentStepId);
}
