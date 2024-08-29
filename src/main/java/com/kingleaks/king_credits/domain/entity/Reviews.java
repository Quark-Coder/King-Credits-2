package com.kingleaks.king_credits.domain.entity;

import com.kingleaks.king_credits.domain.enums.ReviewsStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Table(name = "reviews")
public class Reviews {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "telegram_user_id")
    private Long telegramUserId;

    @Column(name = "payment_check_photo_id")
    private Long paymentCheckPhotoId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ReviewsStatus status;

    @Column(name = "comment")
    private String comment;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
