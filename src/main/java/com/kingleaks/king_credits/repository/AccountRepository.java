package com.kingleaks.king_credits.repository;

import com.kingleaks.king_credits.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByTelegramUserId(Long telegramUserId);

    @Query(value = "SELECT a.balance FROM account a " +
            "WHERE a.telegram_user_id = :telegramUserId ", nativeQuery = true)
    int getBalanceAccountsByTelegramUserId(@Param("telegramUserId") Long telegramUserId);
}
