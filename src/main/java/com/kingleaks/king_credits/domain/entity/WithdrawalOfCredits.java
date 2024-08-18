package com.kingleaks.king_credits.domain.entity;

import com.kingleaks.king_credits.domain.enums.WithdrawalOfCreditsStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "withdrawal_of_credits")
public class WithdrawalOfCredits {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "telegram_user_id")
    private Long telegramUserId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "nick_in_game")
    private String nickInGame;

    @Column(name = "price")
    private Double price;

    @Column(name = "photo", columnDefinition="bytea")
    private byte[] photo;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private WithdrawalOfCreditsStatus status;
}
