package com.kingleaks.king_credits.service;

import com.kingleaks.king_credits.domain.entity.Account;
import com.kingleaks.king_credits.domain.entity.StatePaymentHistory;
import com.kingleaks.king_credits.domain.entity.TelegramUsers;
import com.kingleaks.king_credits.domain.entity.WithdrawalOfCredits;
import com.kingleaks.king_credits.domain.enums.WithdrawalOfCreditsStatus;
import com.kingleaks.king_credits.repository.AccountRepository;
import com.kingleaks.king_credits.repository.TelegramUsersRepository;
import com.kingleaks.king_credits.repository.WithdrawalOfCreditsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WithdrawalOfCreditsService {
    private final CalculateService calculateService;
    private final WithdrawalOfCreditsRepository repository;
    private final TelegramUsersRepository telegramUsersRepository;
    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final StateManagerService stateManager;

    public String createWithdrawalOfCredits(Long telegramUserId, double amount, StatePaymentHistory paymentHistory) {
        TelegramUsers telegramUser = telegramUsersRepository.findByUserId(telegramUserId).orElse(null);
        Account account = accountRepository.findByTelegramUserId(telegramUserId).orElse(null);

        double rubAmount = calculateService.calculateCreditsInRub(amount);
        if (account.getBalance().compareTo(BigDecimal.valueOf(rubAmount)) < 0){
            stateManager.deleteUserState(telegramUserId);
            return "❌ Введено некорректное число кредитов.\n" +
                    "Проверьте, что количество больше, или равно 100 CRDT!\n";
        }


        WithdrawalOfCredits withdrawalOfCredits = new WithdrawalOfCredits();
        withdrawalOfCredits.setTelegramUserId(telegramUserId);
        withdrawalOfCredits.setFirstName(telegramUser.getFirstName());
        withdrawalOfCredits.setLastName(telegramUser.getLastName());
        withdrawalOfCredits.setNickname(telegramUser.getNickname());
        withdrawalOfCredits.setPrice(rubAmount);
        withdrawalOfCredits.setCreatedAt(LocalDateTime.now());
        withdrawalOfCredits.setStatus(WithdrawalOfCreditsStatus.SENT_PHOTO);
        accountService.withdraw(telegramUserId, BigDecimal.valueOf(rubAmount));
        repository.save(withdrawalOfCredits);

        paymentHistory.setStatus("WAITING_FOR_AMOUNT_WITHDRAWAL_PHOTO");
        stateManager.setUserState(telegramUserId, paymentHistory);
        return "❕ Деньги списаны с вашего баланса. \n" +
                "\n" +
                "Выставьте любой скин за " + amount/0.8 + " CRDT\n" +
                "\n" +
                "Отправьте в бота скрин скина с рынка.";
    }

    public void initPhotoWithdrawalOfCredits(Long telegramUserId, byte[] photo) {
        Optional<WithdrawalOfCredits> OptionalWithdrawalOfCredits =
                repository.findByTelegramUserIdAndStatus(telegramUserId, WithdrawalOfCreditsStatus.SENT_PHOTO);

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

    public String getAllListWithdrawalRequest() {
        List<WithdrawalOfCredits> checkPhotoList = repository.getAllWithdrawalOFCreditsByStatusWaiting();
        if (checkPhotoList.isEmpty()) {
            return null;
        }
        StringBuilder result = new StringBuilder();

        for (WithdrawalOfCredits checkPhoto : checkPhotoList) {
            Long id = checkPhoto.getId();
            Double price = checkPhoto.getPrice();
            result.append("чек - ").append(String.format("%05d", id)).append(" сумма кредита - ").append(price).append("\n");
        }

        return result.toString().trim();
    }

    public WithdrawalOfCredits selectWithdrawalOfCreditsById(Long id) {
        Optional<WithdrawalOfCredits> withdrawalOfCredits = repository.findByIdWithStatusPriced(id);
        return withdrawalOfCredits.orElse(null);
    }

    public TelegramUsers confirmRequest(Long id){
        Optional<WithdrawalOfCredits> optionalWithdrawalOfCredits = repository.findById(id);
        if (optionalWithdrawalOfCredits.isPresent()) {
            WithdrawalOfCredits withdrawalOfCredits = optionalWithdrawalOfCredits.get();
            withdrawalOfCredits.setStatus(WithdrawalOfCreditsStatus.PAID);
            repository.save(withdrawalOfCredits);

            return telegramUsersRepository.findByUserId(withdrawalOfCredits.getTelegramUserId()).orElse(null);
        }
        return null;
    }

    public TelegramUsers rejectRequest(Long id){
        Optional<WithdrawalOfCredits> withdrawalOfCredits = repository.findById(id);
        if (withdrawalOfCredits.isPresent()) {
            WithdrawalOfCredits withdrawal = withdrawalOfCredits.get();
            withdrawal.setStatus(WithdrawalOfCreditsStatus.REJECT);
            repository.save(withdrawal);
            return telegramUsersRepository.findByUserId(withdrawal.getTelegramUserId()).orElse(null);
        }
        return null;
    }

    public TelegramUsers errorRequest(Long id){
        Optional<WithdrawalOfCredits> withdrawalOfCredits = repository.findById(id);
        if (withdrawalOfCredits.isPresent()) {
            WithdrawalOfCredits withdrawal = withdrawalOfCredits.get();
            withdrawal.setStatus(WithdrawalOfCreditsStatus.ERROR);
            repository.save(withdrawal);
            return telegramUsersRepository.findByUserId(withdrawal.getTelegramUserId()).orElse(null);
        }
        return null;
    }
}
