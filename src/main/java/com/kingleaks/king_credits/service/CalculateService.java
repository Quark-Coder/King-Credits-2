package com.kingleaks.king_credits.service;

import com.kingleaks.king_credits.domain.entity.SellingRate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CalculateService {
    private final SellingRateService sellingRateService;

    public double calculateCreditsInRub(double amount) {
        SellingRate sellingRate = sellingRateService.getSellingRate();

        if (amount <= 1000 && amount > 0) {
            return amount * sellingRate.getZeroToThousands();
        } else if (amount >= 1001 && amount <= 5000){
            return amount * sellingRate.getThousandToFiveThousand();
        } else if (amount >= 5001 && amount <= 10000){
            return amount * sellingRate.getFiveThousandToTenThousand();
        } else if (amount >= 10001){
            return amount * sellingRate.getTenThousandOrMore();
        }

        return 0.00;
    }

    public double calculateRubInCredits(double amount) {
        SellingRate sellingRate = sellingRateService.getSellingRate();

        if (amount <= 350 && amount > 0) {
            return amount / sellingRate.getZeroToThousands();
        } else if (amount >= 351 && amount <= 1100){
            return amount / sellingRate.getThousandToFiveThousand();
        } else if (amount >= 1101 && amount <= 2000){
            return amount / sellingRate.getFiveThousandToTenThousand();
        } else if (amount >= 2001){
            return amount / sellingRate.getTenThousandOrMore();
        }

        return 0.00;
    }
}
