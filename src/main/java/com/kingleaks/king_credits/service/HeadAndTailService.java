package com.kingleaks.king_credits.service;

import com.kingleaks.king_credits.domain.entity.HeadAndTail;
import com.kingleaks.king_credits.domain.enums.HeadAndTailStatus;
import com.kingleaks.king_credits.repository.HeadAndTailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class HeadAndTailService {
    private final HeadAndTailRepository headAndTailRepository;
    private final AccountService accountService;

    public String game(Long telegramUserId, int amountRate){
        Random random = new Random();
        int chance = random.nextInt(100); // генерируем число от 0 до 99
        HeadAndTail headAndTail = new HeadAndTail();
        headAndTail.setTelegramUserId(telegramUserId);
        headAndTail.setCreatedAt(LocalDateTime.now());

        if (chance < 30) {
            headAndTail.setStatus(HeadAndTailStatus.WIN);
            headAndTail.setAmount(Double.valueOf(amountRate));
            accountService.replenish(telegramUserId, BigDecimal.valueOf(amountRate));
            headAndTailRepository.save(headAndTail);
            return "Да ты везунчик! Твой приз - " + amountRate * 2 +
                    ", мы обновили твой баланс. Спасибо за игру.";
        } else {
            headAndTail.setStatus(HeadAndTailStatus.DEF);
            headAndTail.setAmount(Double.valueOf(amountRate));
            accountService.withdraw(telegramUserId, BigDecimal.valueOf(amountRate));
            headAndTailRepository.save(headAndTail);
            return "Не повезло... Главное не расстраивайся! Повезет в следущий раз.";
        }
    }

}
