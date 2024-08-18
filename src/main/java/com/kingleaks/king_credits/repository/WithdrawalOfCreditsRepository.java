package com.kingleaks.king_credits.repository;

import com.kingleaks.king_credits.domain.entity.WithdrawalOfCredits;
import com.kingleaks.king_credits.domain.enums.WithdrawalOfCreditsStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WithdrawalOfCreditsRepository extends JpaRepository<WithdrawalOfCredits, Long> {
    Optional<WithdrawalOfCredits> findByTelegramUserIdAndStatus(Long telegramUserId, WithdrawalOfCreditsStatus status);
}
