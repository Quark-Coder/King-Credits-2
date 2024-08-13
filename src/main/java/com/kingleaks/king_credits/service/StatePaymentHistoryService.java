package com.kingleaks.king_credits.service;

import com.kingleaks.king_credits.domain.entity.StatePaymentHistory;
import com.kingleaks.king_credits.repository.StatePaymentHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StatePaymentHistoryService {
    private final StatePaymentHistoryRepository statePaymentHistoryRepository;

    public StatePaymentHistory save(StatePaymentHistory statePaymentHistory) {
        return statePaymentHistoryRepository.save(statePaymentHistory);
    }

    public void delete(StatePaymentHistory statePaymentHistory) {
        statePaymentHistoryRepository.delete(statePaymentHistory);
    }

    public StatePaymentHistory createStatePaymentHistory(String state, Long telegramUserId) {

        StatePaymentHistory statePaymentHistory = new StatePaymentHistory();
        statePaymentHistory.setStatus(state);
        statePaymentHistory.setTelegramUserId(telegramUserId);
        return statePaymentHistoryRepository.save(statePaymentHistory);
    }

    public StatePaymentHistory getStatePaymentHistoryByTelegramUserId(Long telegramUserId) {
        Optional<StatePaymentHistory> statePaymentHistory = statePaymentHistoryRepository.findById(telegramUserId);
        return statePaymentHistory.orElse(null);
    }
}
