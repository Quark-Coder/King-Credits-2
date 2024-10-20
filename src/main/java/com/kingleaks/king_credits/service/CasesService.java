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

@Service
@RequiredArgsConstructor
public class CasesService {
    private final CaseInventoryService caseInventoryService;
    private final CaseInventoryRepository caseInventoryRepository;
    private final CasesItemRepository casesItemRepository;
    private final CasesRepository casesRepository;

    public String getAllCasesNameAndPrice(){
        String result = "";
        List<Cases> casesList = casesRepository.findAll();
        if (casesList.isEmpty()) {
            result = "Нету активных кейсов";
        }

        for (Cases cases : casesList){
            result = result +"\n№" + cases.getId() + " "
                    + cases.getName() + " " + cases.getPrice() + " руб";
        }

        return result;
    }

    public Cases getCasesByIdForPicture(Long id){
        Optional<Cases> result = casesRepository.findById(id);
        if (result.isPresent()) {
            return result.get();
        }

        return null;
    }

    public String getCasesById(Long id){
        Optional<Cases> optionalCases = casesRepository.findById(id);
        if (optionalCases.isPresent()) {
            String result = "";
            Cases cases = optionalCases.get();
            List<CasesItem> casesItemList = casesItemRepository.findAllCasesItemByCaseName(cases.getName());
            result = result + cases.getName() + " " + cases.getPrice() + " Руб. Хороший выбор! Вот его содержимое:";

            for (CasesItem casesItem : casesItemList){
                result = result + "\n" + " " + casesItem.getName() + " - " + casesItem.getPrice() + " Кредитов (Шанс " + casesItem.getCoefficient() + "%)";
            }

            return result;
        }

        return null;
    }

    public String getAllCasesUser(Long telegramUserId){
        List<CaseInventory> caseInventories = caseInventoryService.getInventoryUser(telegramUserId);

        String result = "";
        for (CaseInventory caseInventory : caseInventories){
            Cases cases = casesRepository.findById(caseInventory.getCaseId()).orElse(null);
            result = result + "\n№" + caseInventory.getId() + " " + cases.getName();
        }

        return result;
    }

    public String selectCaseForOpen(Long inventoryId) {
        Optional<CaseInventory> optionalCasesInventory = caseInventoryRepository.findById(inventoryId);

        if (optionalCasesInventory.isPresent()){
            CaseInventory caseInventory = optionalCasesInventory.get();
            return getCasesById(caseInventory.getCaseId());
        }

        return null;
    }

    public String getCasesListWithoutPicAsString() {
        List<Cases> casesList = casesRepository.findAllCasesWithoutPicture();
        if (casesList.isEmpty()){
            return null;
        }
        String result = "";
        for (Cases cases : casesList) {
            result = result + "№" + cases.getId() + " " + cases.getName() + "\n";
        }
        return result;
    }

    public void savePictureForCase(Long itemId, byte[] picture){
        Optional<Cases> optionalCases = casesRepository.findById(itemId);

        if (optionalCases.isPresent()){
            Cases cases = optionalCases.get();
            cases.setPhotoData(picture);
            casesRepository.save(cases);
        }
    }
}
