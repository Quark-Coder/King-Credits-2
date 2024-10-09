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
import java.util.List;
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

            return "• Ваш никнейм - " + nickname +
                    "\n• ID пользователя - "  + String.format("%05d", id) +
                    "\n\uD83D\uDED2 Ваш счет:\n" +
                    "\n• Баланс - " + balance +
                    "\n• Всего пополнено - " + replenish +
                    "\n• Всего выведено - " + withdrew;
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

    public List<TelegramUsers> findAllAdmins(){
        return telegramUsersRepository.findAllAdmins();
    }

    public String getLeaderboard() {
        List<TelegramUsers> telegramUsers = telegramUsersRepository.findAllTelegramUsersForLeaderBoard();
        String result = "";
        int i = 1;
        for (TelegramUsers telegramUser : telegramUsers){
            result += "\n" + i + ". " + (telegramUser.getNickname() == null ? telegramUser.getFirstName() :
                    telegramUser.getNickname()) + " - выведено: " +
                    withdrawalOfCreditsRepository.countAmountWithdrawalOfCreditsByPAID(telegramUser.getUserId()) + " кредитов";
            i++;
        }

        return result;
    }

    public String getAllUsersWithPagination(int page){
        List<TelegramUsers> telegramUsers = telegramUsersRepository.findAllUsers();

        int pageSize = 10;
        int fromIndex = (page - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, telegramUsers.size());

        if (fromIndex > telegramUsers.size()) {
            return null; // Возвращаем пустой список, если страница выходит за пределы
        }

        List<TelegramUsers> paginationList = telegramUsers.subList(fromIndex, toIndex);

        String result = "";
        for (TelegramUsers telegramUser : paginationList){
            result += telegramUser.getId() + " - " + telegramUser.getFirstName()
                    + " " + telegramUser.getNickname() + "\n";
        }

        return result;
    }

    public String getBlackListWithPagination(int page){
        List<TelegramUsers> telegramUsers = telegramUsersRepository.findAllUsersInBlackList();

        int pageSize = 10;
        int fromIndex = (page - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, telegramUsers.size());

        if (fromIndex > telegramUsers.size()) {
            return null; // Возвращаем пустой список, если страница выходит за пределы
        }

        List<TelegramUsers> paginationList = telegramUsers.subList(fromIndex, toIndex);

        String result = "";
        for (TelegramUsers telegramUser : paginationList){
            result += telegramUser.getId() + " - " + telegramUser.getFirstName()
                    + " " + telegramUser.getNickname() + "\n";
        }

        return result;
    }

    public String getInformationUserProfileForAdmin(Long telegramUserId){
        Optional<TelegramUsers> telegramUsers = telegramUsersRepository.findById(telegramUserId);
        if (telegramUsers.isPresent()){
            TelegramUsers telegramUser = telegramUsers.get();
            Long id = telegramUser.getId();
            String nickname = telegramUser.getNickname();
            BigDecimal balance = telegramUsersRepository.getBalanceByUserId(telegramUser.getUserId());
            int replenish = paymentCheckPhotoRepository.countAmountPaymentCheckPhotoByCONFIRMED(telegramUser.getUserId());
            int withdrew = withdrawalOfCreditsRepository.countAmountWithdrawalOfCreditsByPAID(telegramUser.getUserId());

            return "Никнейм - " + nickname +
                    "\nАйди - "  + String.format("%05d", id) +
                    "\nБаланс - " + balance +
                    "\nВсего пополнено - " + replenish +
                    "\nВсего выведено - " + withdrew;
        }

        return null;
    }

    public void banUser(Long userId) {
        Optional<TelegramUsers> telegramUsers = telegramUsersRepository.findById(userId);
        if (telegramUsers.isPresent()){
            TelegramUsers telegramUser = telegramUsers.get();
            telegramUser.setStatus(UserStatus.BANNED);
            telegramUsersRepository.save(telegramUser);
        }
    }

    public void unBanUser(Long userId) {
        Optional<TelegramUsers> telegramUsers = telegramUsersRepository.findById(userId);
        if (telegramUsers.isPresent()){
            TelegramUsers telegramUser = telegramUsers.get();
            telegramUser.setStatus(UserStatus.USER);
            telegramUsersRepository.save(telegramUser);
        }
    }

    public String getStatisticForPeriod(int period){
            int replenish = paymentCheckPhotoRepository.countAmountPaymentCheckPhotoByCONFIRMEDForPeriod(period);
            int withdrew = withdrawalOfCreditsRepository.countAmountWithdrawalOfCreditsByPAIDForPeriod(period);
            int users = telegramUsersRepository.countUsersForPeriod(period);

        return "Статистика за " + period + " дней" +
                "\nПолучено рублей - " + replenish +
                "\nВыведено кредитов - " + withdrew +
                "\nНовых пользователей - " + users;
    }

    public String getStatisticForAllTime(){
        int replenish = paymentCheckPhotoRepository.countAmountPaymentCheckPhotoByCONFIRMEDForAllTime();
        int withdrew = withdrawalOfCreditsRepository.countAmountWithdrawalOfCreditsByPAIDForAllTime();
        int users = telegramUsersRepository.countUsersForAllTime();

        return "Статистика за все время" +
                "\nПолучено рублей - " + replenish +
                "\nВыведено кредитов - " + withdrew +
                "\nНовых пользователей - " + users;
    }

    public boolean isBanned(Long telegramUserId) {
        return telegramUsersRepository.isUserBanned(telegramUserId);
    }
}
