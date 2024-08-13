package com.kingleaks.king_credits.service;

import com.kingleaks.king_credits.domain.entity.StatePaymentHistory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class StateManager {
    private final Map<Long, StatePaymentHistory> userStates = new HashMap<>();

    public StatePaymentHistory getUserState(Long telegramUserId) {
        return userStates.get(telegramUserId);
    }

    public void setUserState(Long telegramUserId, StatePaymentHistory userState) {
        userStates.put(telegramUserId, userState);
    }

    public void deleteUserState(Long telegramUserId) {
        userStates.remove(telegramUserId);
    }
}
