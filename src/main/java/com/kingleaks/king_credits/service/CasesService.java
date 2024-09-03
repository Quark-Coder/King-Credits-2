package com.kingleaks.king_credits.service;

import com.kingleaks.king_credits.domain.entity.Cases;
import com.kingleaks.king_credits.domain.entity.CasesItem;
import com.kingleaks.king_credits.repository.CasesItemRepository;
import com.kingleaks.king_credits.repository.CasesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CasesService {
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

    public String getCasesById(Long id){
        Optional<Cases> optionalCases = casesRepository.findById(id);
        if (optionalCases.isPresent()) {
            String result = "";
            Cases cases = optionalCases.get();
            List<CasesItem> casesItemList = casesItemRepository.findAllCasesItemByCaseName(cases.getName().name());
            result = result + "№" + cases.getId() + " " + cases.getName() + " " + cases.getPrice() + " Руб. Хороший выбор! Вот его содержимое:";

            for (CasesItem casesItem : casesItemList){
                result = result + "\n" + " " + casesItem.getName() + " - " + casesItem.getPrice() + "Кредитов (Шанс " + casesItem.getCoefficient() + "%)";
            }

            return result;
        }

        return null;
    }
}
