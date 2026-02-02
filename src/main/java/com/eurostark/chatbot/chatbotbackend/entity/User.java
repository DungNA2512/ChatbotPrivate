package com.eurostark.chatbot.chatbotbackend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // định danh phía client (cookie / token / deviceId)
    @Column(unique = true, nullable = false)
    private String externalId;

    private String name;
    private String email;
}
