package com.kingleaks.king_credits.service;

import com.kingleaks.king_credits.domain.entity.Account;
import com.kingleaks.king_credits.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public void createAccount(Long telegramUserId) {
        Account account = new Account();
        account.setTelegramUserId(telegramUserId);
        account.setBalance(BigDecimal.valueOf(0.0));
        accountRepository.save(account);
    }
}
