package com.kingleaks.king_credits.domain.entity;

import com.kingleaks.king_credits.domain.enums.HeadAndTailStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Table(name = "head_and_tail")
public class HeadAndTail {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "telegram_user_id")
    private Long telegramUserId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "amount")
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private HeadAndTailStatus status;
}
