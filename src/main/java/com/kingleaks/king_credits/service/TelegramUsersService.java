package com.kingleaks.king_credits.service;

import com.kingleaks.king_credits.domain.entity.TelegramUsers;
import com.kingleaks.king_credits.domain.enums.UserStatus;
import com.kingleaks.king_credits.repository.TelegramUsersRepository;
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

    public void registerUser(Long userId, String firstName, String lastName, String nickname) {
        if (telegramUsersRepository.findByUserId(userId).isEmpty()) {
            TelegramUsers newUser = new TelegramUsers();
            newUser.setUserId(userId);
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
            int replenish = 0;
            int withdrew = 0;

            return "Ваш никнейм - " + nickname +
                    "\nАйди - "  + id +
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
}
