package com.kingleaks.king_credits.cronjob;

import com.kingleaks.king_credits.domain.entity.PromoCode;
import com.kingleaks.king_credits.domain.entity.Reviews;
import com.kingleaks.king_credits.domain.entity.TelegramUsers;
import com.kingleaks.king_credits.domain.enums.PromoCodeStatus;
import com.kingleaks.king_credits.repository.PromoCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@EnableScheduling
@Service
@Slf4j
@RequiredArgsConstructor
public class CheckExpiredPromoCode {
    private final PromoCodeRepository promoCodeRepository;
    private static final String GMT_3 = "GMT+3";

    @Scheduled(cron = "0 0 0 * * *", zone = GMT_3)
    public void checkPromoCode() {
        List<PromoCode> expiredPromoCode = promoCodeRepository.getAllExpiredPromoCodeWithStatusActive();
        if (!expiredPromoCode.isEmpty()) {
            for (PromoCode promoCode : expiredPromoCode){
                promoCode.setStatus(PromoCodeStatus.EXPIRED);
                promoCodeRepository.save(promoCode);
            }
            log.info("Проверили список просроченных промокодов");
        }
        log.info("Проверили список промокодов");
    }

}
