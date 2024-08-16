package com.kingleaks.king_credits.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name="payment_check_photo")
public class PaymentCheckPhoto {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "telegram_user_id")
    private Long telegramUserId;

    @Column(name = "photo_data", columnDefinition="bytea")
    private byte[] photoData;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
