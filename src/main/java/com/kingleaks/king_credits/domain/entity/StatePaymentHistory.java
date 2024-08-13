package com.kingleaks.king_credits.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="state_payment_history")
public class StatePaymentHistory {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "status")
    private String status;

    @Column(name = "telegram_user_id")
    private Long telegramUserId;
}
