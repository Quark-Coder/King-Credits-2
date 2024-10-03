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
import java.util.List;
import java.util.Optional;

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
        if (code.equals("/home")){
            return "";
        }
        if (!promoCodeRepository.existsByCode(code)){
            return "Ошибка! Промокод не найден";
        }
        if (promoCodeRepository.isExpiredDate(code)){
            return "Промокод неактивен";
        }
        if (!usedPromoCodesRepository.existsByCountUser(code)){
            return "Количество использовании превышает";
        }
        if (Boolean.TRUE.equals(usedPromoCodesRepository.userHasPromoCode(telegramUserId, code))){
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

    public String getAllActivePromoCodes() {
        List<PromoCode> promoCodes = promoCodeRepository.getAllActivePromoCodes();

        if (promoCodes.isEmpty()){
            return "Список пуст";
        }
        String result = "";
        for (PromoCode promoCode : promoCodes) {
            result += "№" + promoCode.getId() + " - " + promoCode.getCode() + " " + promoCode.getPrize() + " Руб\n";
        }
        return result;
    }

    public String getPromoCodeById(long id) {
        Optional<PromoCode> optionalPromoCode = promoCodeRepository.findById(id);
        if (optionalPromoCode.isPresent()){
            PromoCode promoCode = optionalPromoCode.get();
            int countUsedPromoCode = usedPromoCodesRepository.countUsedPromo(promoCode.getCode());
            return "№" + promoCode.getId() + " " + promoCode.getCode()
                    + " дата окончания " + promoCode.getCreatedAt() + " осталось применений " + countUsedPromoCode;
        }

        return null;
    }

    public void deletePromoCodeById(long id) {
        Optional<PromoCode> optionalPromoCode = promoCodeRepository.findById(id);
        if (optionalPromoCode.isPresent()){
            PromoCode promoCode = optionalPromoCode.get();
            promoCode.setStatus(PromoCodeStatus.EXPIRED);
            promoCodeRepository.save(promoCode);
        }
    }
}
