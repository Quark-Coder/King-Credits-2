package com.kingleaks.king_credits.service;

import com.kingleaks.king_credits.domain.entity.TelegramUsers;
import com.kingleaks.king_credits.domain.entity.WithdrawalOfCredits;
import com.kingleaks.king_credits.domain.enums.WithdrawalOfCreditsStatus;
import com.kingleaks.king_credits.repository.TelegramUsersRepository;
import com.kingleaks.king_credits.repository.WithdrawalOfCreditsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WithdrawalOfCreditsService {
    private final WithdrawalOfCreditsRepository repository;
    private final TelegramUsersRepository telegramUsersRepository;

    public void createWithdrawalOfCredits(Long telegramUserId, double amount) {
        TelegramUsers telegramUser = telegramUsersRepository.findByUserId(telegramUserId).orElse(null);

        WithdrawalOfCredits withdrawalOfCredits = new WithdrawalOfCredits();
        withdrawalOfCredits.setTelegramUserId(telegramUserId);
        withdrawalOfCredits.setFirstName(telegramUser.getFirstName());
        withdrawalOfCredits.setLastName(telegramUser.getLastName());
        withdrawalOfCredits.setNickname(telegramUser.getNickname());
        withdrawalOfCredits.setPrice(amount);
        withdrawalOfCredits.setCreatedAt(LocalDateTime.now());
        withdrawalOfCredits.setStatus(WithdrawalOfCreditsStatus.SENT_PHOTO);
        repository.save(withdrawalOfCredits);
    }

    public void initPhotoWithdrawalOfCredits(Long telegramUserId, byte[] photo) {
        Optional<WithdrawalOfCredits> OptionalWithdrawalOfCredits =
                repository.findByTelegramUserIdAndStatus(telegramUserId, WithdrawalOfCreditsStatus.SENT_PHOTO);

        System.out.println(photo.length);
        if (OptionalWithdrawalOfCredits.isPresent()) {
            WithdrawalOfCredits withdrawalOfCredits = OptionalWithdrawalOfCredits.get();
            withdrawalOfCredits.setPhoto(photo);
            withdrawalOfCredits.setStatus(WithdrawalOfCreditsStatus.SENT_NICK);
            repository.save(withdrawalOfCredits);
        }
    }

    public void initNickInGameWithdrawalOfCredits(Long telegramUserId, String nick) {
        Optional<WithdrawalOfCredits> OptionalWithdrawalOfCredits =
                repository.findByTelegramUserIdAndStatus(telegramUserId, WithdrawalOfCreditsStatus.SENT_NICK);

        if (OptionalWithdrawalOfCredits.isPresent()) {
            WithdrawalOfCredits withdrawalOfCredits = OptionalWithdrawalOfCredits.get();
            withdrawalOfCredits.setNickInGame(nick);
            withdrawalOfCredits.setStatus(WithdrawalOfCreditsStatus.WAITING);
            repository.save(withdrawalOfCredits);
        }
    }
}
