package com.eurostark.chatbot.chatbotbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.eurostark.chatbot.chatbotbackend.entity.LeaveRequest;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

}
