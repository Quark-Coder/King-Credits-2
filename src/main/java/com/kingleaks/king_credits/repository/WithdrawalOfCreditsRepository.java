package com.kingleaks.king_credits.repository;

import com.kingleaks.king_credits.domain.entity.PaymentCheckPhoto;
import com.kingleaks.king_credits.domain.entity.WithdrawalOfCredits;
import com.kingleaks.king_credits.domain.enums.WithdrawalOfCreditsStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WithdrawalOfCreditsRepository extends JpaRepository<WithdrawalOfCredits, Long> {
    Optional<WithdrawalOfCredits> findByTelegramUserIdAndStatus(Long telegramUserId, WithdrawalOfCreditsStatus status);

    @Query(value = "SELECT * FROM withdrawal_of_credits w " +
            "where w.status = 'WAITING' ORDER BY w.created_at DESC", nativeQuery = true)
    List<WithdrawalOfCredits> getAllWithdrawalOFCreditsByStatusWaiting();

    @Query(value = "SELECT * FROM withdrawal_of_credits w " +
            "where w.status = 'WAITING' and w.id = :id", nativeQuery = true)
    Optional<WithdrawalOfCredits> findByIdWithStatusPriced(@Param("id") Long id);

    @Query(value = "SELECT COALESCE(SUM(w.price), 0) FROM withdrawal_of_credits w " +
            "WHERE w.status = 'PAID' AND w.telegram_user_id = :telegramUserId ", nativeQuery = true)
    int countAmountWithdrawalOfCreditsByPAID(@Param("telegramUserId") Long telegramUserId);

    @Query(value = "SELECT COALESCE(SUM(w.price), 0) FROM withdrawal_of_credits w " +
            "WHERE w.status = 'PAID' AND w.created_at >= NOW() - INTERVAL '1 day' * :period ", nativeQuery = true)
    int countAmountWithdrawalOfCreditsByPAIDForPeriod(@Param("period") int period);

    @Query(value = "SELECT COALESCE(SUM(w.price), 0) FROM withdrawal_of_credits w " +
            "WHERE w.status = 'PAID' ", nativeQuery = true)
    int countAmountWithdrawalOfCreditsByPAIDForAllTime();
}
