package com.kingleaks.king_credits.service;

import com.kingleaks.king_credits.domain.entity.SellingRate;
import com.kingleaks.king_credits.repository.SellingRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SellingRateService {
    private final SellingRateRepository sellingRateRepository;

    public String getSellingRate() {
        Optional<SellingRate> optionalSellingRate = sellingRateRepository.findById(1L);

        if (optionalSellingRate.isPresent()) {
            SellingRate sellingRate = optionalSellingRate.get();

            return "Тут вы сможете приобрести кредиты по максимально выгодному курсу: \n\n" +
                    "от 0 до 1000 / " + sellingRate.getZeroToThousands() + "₽\n" +
                    "от 1000 до 5000 / " + sellingRate.getThousandToFiveThousand() + "₽\n" +
                    "от 5000 до 10000 / " + sellingRate.getFiveThousandToTenThousand() + "₽\n" +
                    "от 10000 и более / " + sellingRate.getTenThousandOrMore() +"₽\n\n" +
                    "Комиссия на нас!";
        }

        return null;
    }
}
