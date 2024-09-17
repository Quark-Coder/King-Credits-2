package com.kingleaks.king_credits.service;

import com.kingleaks.king_credits.domain.entity.PromoCode;
import com.kingleaks.king_credits.domain.enums.PromoCodeStatus;
import com.kingleaks.king_credits.repository.PromoCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PromoCodeService {
    private final PromoCodeRepository promoCodeRepository;

    public PromoCode createPromoCode(String code, LocalDate endDate, int numberOfUses, double amount) {
        if (promoCodeRepository.existsByCode(code)){
            return null;
        }

        PromoCode promoCode = new PromoCode();
        promoCode.setCode(code);
        promoCode.setCreatedAt(LocalDate.now());
        promoCode.setEndDate(endDate);
        promoCode.setCountUsers(numberOfUses);
        promoCode.setPrize(amount);
        promoCode.setStatus(PromoCodeStatus.ACTIVE);
        return promoCodeRepository.save(promoCode);
    }
}
