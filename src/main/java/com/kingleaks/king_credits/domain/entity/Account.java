package com.kingleaks.king_credits.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Setter
@Getter
@Table(name = "account")
public class Account {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "telegram_user_id")
    private Long telegramUserId;

    @Column(name = "balance")
    private BigDecimal balance;
}
