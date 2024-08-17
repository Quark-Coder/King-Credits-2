package com.kingleaks.king_credits.domain.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatePaymentHistory {
    private Long id;

    private String status;

    private Long telegramUserId;
}
