package com.kingleaks.king_credits.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Table(name = "used_promo_codes")
public class UsedPromoCodes {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "telegram_user_id")
    private Long telegramUserId;
}
