package com.kingleaks.king_credits.service;

import com.kingleaks.king_credits.domain.entity.Account;
import com.kingleaks.king_credits.domain.entity.CaseInventory;
import com.kingleaks.king_credits.domain.entity.Cases;
import com.kingleaks.king_credits.repository.AccountRepository;
import com.kingleaks.king_credits.repository.CaseInventoryRepository;
import com.kingleaks.king_credits.repository.CasesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CaseInventoryService {
    private final CaseInventoryRepository caseInventoryRepository;
    private final CasesRepository casesRepository;
    private final AccountRepository accountRepository;
    private final AccountService accountService;

    public String saveInventory(Long caseId, Long telegramUserId) {
        Optional<Cases> optionalCases = casesRepository.findById(caseId);
        if (optionalCases.isPresent()){
            Cases cases = optionalCases.get();
            if (accountRepository.getBalanceAccountsByTelegramUserId(telegramUserId) < cases.getPrice()){
                return "У вас недостаточно средств, пополните баланс";
            }

            accountService.withdraw(telegramUserId, BigDecimal.valueOf(cases.getPrice()));
            CaseInventory caseInventory = new CaseInventory();
            caseInventory.setCaseId(caseId);
            caseInventory.setTelegramUserId(telegramUserId);
            caseInventoryRepository.save(caseInventory);
            return "Спасибо за покупку! Открыть кейс вы можете зайдя во вкладку \"Мои кейсы\"";
        } else {
            return "Вы не правильно указали номер кейса";
        }
    }

    public List<CaseInventory> getInventoryUser(Long telegramUserId) {
        return caseInventoryRepository.findAllByTelegramUserId(telegramUserId);
    }
}
