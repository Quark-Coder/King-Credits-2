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

    public String getSellingRateString() {
        Optional<SellingRate> optionalSellingRate = sellingRateRepository.findById(1L);

        if (optionalSellingRate.isPresent()) {
            SellingRate sellingRate = optionalSellingRate.get();

            return "\uD83D\uDCC8 Актуальный курс \n\n" +
                    "▫\uFE0F Курс: " + sellingRate.getTenThousandOrMore() +"₽ за 1 кредит\n" +
                    "(Комиссия на нас)\n" +
                    "Перед покупкой, обязательно " +
                    "<a href=\"https://telegra.ph/Usloviya-pered-pokupkoj--prodazhej-09-19\">ознакомьтесь с условиями – нажми для ознакомления </a>";
        }

        return null;
    }

    public SellingRate getSellingRate() {
        Optional<SellingRate> optionalSellingRate = sellingRateRepository.findById(1L);
        return optionalSellingRate.orElse(null);

    }
}
