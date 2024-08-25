package com.kingleaks.king_credits.repository;

import com.kingleaks.king_credits.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByTelegramUserId(Long telegramUserId);


}
