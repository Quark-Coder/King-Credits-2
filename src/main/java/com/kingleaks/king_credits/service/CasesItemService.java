package com.kingleaks.king_credits.service;

import com.kingleaks.king_credits.domain.entity.CaseInventory;
import com.kingleaks.king_credits.domain.entity.Cases;
import com.kingleaks.king_credits.domain.entity.CasesItem;
import com.kingleaks.king_credits.repository.CaseInventoryRepository;
import com.kingleaks.king_credits.repository.CasesItemRepository;
import com.kingleaks.king_credits.repository.CasesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CasesItemService {
    private final CasesItemRepository casesItemRepository;
    private final CasesRepository casesRepository;
    private final CaseInventoryRepository caseInventoryRepository;

    public CasesItem getRandomItem(Long inventoryId){
        CaseInventory caseInventory = caseInventoryRepository.findById(inventoryId).orElse(null);

        Optional<Cases> optionalCases = casesRepository.findById(caseInventory.getCaseId());
        if (optionalCases.isPresent()){
            Cases cases = optionalCases.get();
            String caseName = cases.getName().name();
            List<CasesItem> itemList = casesItemRepository.findAllCasesItemByCaseName(caseName);
            caseInventoryRepository.delete(caseInventory);
            return selectCaseItem(itemList);
        }

        return null;
    }


    public CasesItem selectCaseItem(List<CasesItem> itemList) {
        // Генерация случайного числа от 0 до 100
        Random random = new Random();
        double randomNumber = random.nextDouble() * 100;

        double cumulativeProbability = 0.0;

        for (CasesItem item : itemList) {
            // Накопление вероятности
            cumulativeProbability += item.getCoefficient();

            // Если случайное число попало в текущий диапазон, возвращаем элемент
            if (randomNumber <= cumulativeProbability) {
                return item;
            }
        }

        // На случай, если не сработал ни один элемент (что маловероятно)
        return null;
    }

    public String getItemListAsString() {
        List<CasesItem> itemList = casesItemRepository.findAllItemCasesWithoutPicture();
        if (itemList.isEmpty()){
            return null;
        }
        String result = "";
        for (CasesItem item : itemList) {
            result = result + "№" + item.getId() + " " + item.getName() + "\n";
        }
        return result;
    }

    public void savePictureForItem(Long itemId, byte[] picture){
        Optional<CasesItem> optionalCasesItem = casesItemRepository.findById(itemId);

        if (optionalCasesItem.isPresent()){
            CasesItem casesItem = optionalCasesItem.get();
            casesItem.setPhotoData(picture);
            casesItemRepository.save(casesItem);
        }
    }
}
