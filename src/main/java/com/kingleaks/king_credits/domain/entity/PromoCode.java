package com.kingleaks.king_credits.domain.entity;

import com.kingleaks.king_credits.domain.enums.PromoCodeStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Setter
@Getter
@Table(name = "promo_code")
public class PromoCode {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "count_users")
    private Integer countUsers;

    @Column(name = "prize")
    private Double prize;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PromoCodeStatus status;
}
