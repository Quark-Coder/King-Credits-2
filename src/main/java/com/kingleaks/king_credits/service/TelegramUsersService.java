package com.kingleaks.king_credits.service;

import com.kingleaks.king_credits.domain.entity.TelegramUsers;
import com.kingleaks.king_credits.domain.enums.UserStatus;
import com.kingleaks.king_credits.repository.PaymentCheckPhotoRepository;
import com.kingleaks.king_credits.repository.TelegramUsersRepository;
import com.kingleaks.king_credits.repository.WithdrawalOfCreditsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TelegramUsersService {
    private final TelegramUsersRepository telegramUsersRepository;
    private final AccountService accountService;
    private final PaymentCheckPhotoRepository paymentCheckPhotoRepository;
    private final WithdrawalOfCreditsRepository withdrawalOfCreditsRepository;

    public void registerUser(Long userId, Long chatId, String firstName, String lastName, String nickname) {
        if (telegramUsersRepository.findByUserId(userId).isEmpty()) {
            TelegramUsers newUser = new TelegramUsers();
            newUser.setUserId(userId);
            newUser.setChatId(chatId);
            newUser.setFirstName(firstName);
            newUser.setLastName(lastName);
            newUser.setNickname(nickname);
            newUser.setCreatedAt(LocalDateTime.now());
            newUser.setStatus(UserStatus.USER);
            telegramUsersRepository.save(newUser);
            accountService.createAccount(userId);
        }
    }

    public String getInformationForProfile(Long telegramUserId){
        Optional<TelegramUsers> telegramUsers = telegramUsersRepository.findByUserId(telegramUserId);
        if (telegramUsers.isPresent()){
            TelegramUsers telegramUser = telegramUsers.get();
            Long id = telegramUser.getId();
            String nickname = telegramUser.getNickname();
            BigDecimal balance = telegramUsersRepository.getBalanceByUserId(telegramUserId);
            int replenish = paymentCheckPhotoRepository.countAmountPaymentCheckPhotoByCONFIRMED(telegramUserId);
            int withdrew = withdrawalOfCreditsRepository.countAmountWithdrawalOfCreditsByPAID(telegramUserId);

            return "Ваш никнейм - " + nickname +
                    "\nАйди - "  + String.format("%05d", id) +
                    "\nБаланс - " + balance +
                    "\nВсего пополнено - " + replenish +
                    "\nВсего выведено - " + withdrew;
        }

        return null;
    }

    public void changeNickname(Long telegramUserId, String newNickname){
        Optional<TelegramUsers> telegramUsers = telegramUsersRepository.findByUserId(telegramUserId);
        if (telegramUsers.isPresent()){
            TelegramUsers telegramUser = telegramUsers.get();
            telegramUser.setNickname(newNickname);
            telegramUsersRepository.save(telegramUser);
        }
    }

    public UserStatus getStatus(Long telegramUserId) {
        return telegramUsersRepository.getUserStatusByUserId(telegramUserId);
    }

    public TelegramUsers findById(Long telegramUserId) {
        Optional<TelegramUsers> telegramUsers = telegramUsersRepository.findByUserId(telegramUserId);
        return telegramUsers.orElse(null);
    }
}
