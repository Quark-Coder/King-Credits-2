package com.kingleaks.king_credits.service;

import com.kingleaks.king_credits.domain.entity.PromoCode;
import com.kingleaks.king_credits.domain.entity.UsedPromoCodes;
import com.kingleaks.king_credits.domain.enums.PromoCodeStatus;
import com.kingleaks.king_credits.repository.PromoCodeRepository;
import com.kingleaks.king_credits.repository.UsedPromoCodesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PromoCodeService {
    private final AccountService accountService;
    private final UsedPromoCodesRepository usedPromoCodesRepository;
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

    public String enterPromoCode(String code, long telegramUserId){
        if (promoCodeRepository.existsByCode(code) || promoCodeRepository.isExpiredDate(code)){
            return "Ошибка! Промокод не найден, либо неактивен";
        }
        if (!usedPromoCodesRepository.existsByCountUser(code)){
            return "Количество использовании превышает";
        }
        if (usedPromoCodesRepository.userHasPromoCode(telegramUserId, code)){
            return "Вы уже активировали этот промокод";
        }

        PromoCode promoCode = promoCodeRepository.findByCode(code);
        accountService.replenish(telegramUserId, BigDecimal.valueOf(promoCode.getPrize()));

        UsedPromoCodes usedPromoCodes = new UsedPromoCodes();
        usedPromoCodes.setCode(code);
        usedPromoCodes.setTelegramUserId(telegramUserId);
        usedPromoCodes.setCreatedAt(LocalDate.now());
        usedPromoCodesRepository.save(usedPromoCodes);

        return "Промокод успешно активирован! Деньги зачислены на ваш баланс.";
    }
}
