package com.eurostark.chatbot.chatbotbackend.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import com.eurostark.chatbot.chatbotbackend.entity.LeaveRequest;
import com.eurostark.chatbot.chatbotbackend.repository.LeaveRequestRepository;

@Slf4j
@Service
public class LeaveRequestService {

    private final LeaveRequestRepository repo;

    public LeaveRequestService(LeaveRequestRepository repo) {
        this.repo = repo;
    }

    public void saveLeave(String sessionId, String leaveDate) {

        if (leaveDate == null || leaveDate.isBlank()) {
            log.error("❌ Cannot save leave: leaveDate is null. session={}", sessionId);
            throw new IllegalStateException("leaveDate is missing");
        }

        LocalDate parsedDate;
        try {
            parsedDate = LocalDate.parse(leaveDate);
        } catch (Exception e) {
            log.error("❌ Invalid leaveDate format: {}", leaveDate, e);
            throw new IllegalArgumentException("Invalid date format");
        }

        LeaveRequest req = new LeaveRequest();
        req.setSessionId(sessionId);
        req.setLeaveDate(parsedDate);
        req.setStatus("PENDING");

        repo.save(req);

        log.info("✅ Leave request saved. session={}, date={}", sessionId, parsedDate);
    }
}