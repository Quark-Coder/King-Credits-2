package com.kingleaks.king_credits.service;

import com.kingleaks.king_credits.domain.entity.CaseInventory;
import com.kingleaks.king_credits.repository.CaseInventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CaseInventoryService {
    private final CaseInventoryRepository caseInventoryRepository;

    public void saveInventory(Long caseId, Long telegramUserId) {
        CaseInventory caseInventory = new CaseInventory();
        caseInventory.setCaseId(caseId);
        caseInventory.setTelegramUserId(telegramUserId);
        caseInventoryRepository.save(caseInventory);
    }
}
