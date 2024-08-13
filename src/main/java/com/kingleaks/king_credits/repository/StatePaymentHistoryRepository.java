package com.kingleaks.king_credits.repository;

import com.kingleaks.king_credits.domain.entity.StatePaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatePaymentHistoryRepository extends JpaRepository<StatePaymentHistory, Long> {
    Optional<StatePaymentHistory> findByTelegramUserId(Long telegramUserId);
}
