package com.eurostark.chatbot.chatbotbackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Basic;
import jakarta.persistence.FetchType;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code; // LEAVE_POLICY

    private String title; // "Quy định nghỉ phép"
    private String fileName; // leave-policy.pdf
    private String contentType; // application/pdf

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] data;

    private boolean active = true;

}
