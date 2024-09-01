package com.kingleaks.king_credits.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "case_inventory")
public class CaseInventory {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "case_id")
    private Long caseId;

    @Column(name = "telegram_user_id")
    private Long TelegramUserId;
}
