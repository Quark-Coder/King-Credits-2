package com.kingleaks.king_credits.bot.waitingState;

import com.kingleaks.king_credits.domain.entity.StatePaymentHistory;
import com.kingleaks.king_credits.domain.entity.TelegramUsers;
import com.kingleaks.king_credits.repository.TelegramUsersRepository;
import com.kingleaks.king_credits.service.AccountService;
import com.kingleaks.king_credits.service.StateManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class StateWaitingForWithdrawRubAccount implements StateWaitingQueryHandler{
    private final AccountService accountService;
    private final TelegramUsersRepository telegramUsersRepository;
    private final StateManagerService stateManager;

    public SendMessage waitingForWithRubAccount(StatePaymentHistory paymentHistory,
                                        Long chatId, String messageText, Long telegramUserID){
        if (paymentHistory != null){
            try {
                SendMessage message = new SendMessage();
                message.setChatId(chatId);

                String[] parts = paymentHistory.getStatus().split("__");
                Long userId = Long.parseLong(parts[1]);
                BigDecimal balance = telegramUsersRepository.getBalanceByAccountId(userId);
                TelegramUsers telegramUsers = telegramUsersRepository.findById(userId).orElse(null);
                double amount = Double.parseDouble(messageText);

                if (balance.compareTo(BigDecimal.valueOf(amount)) < 0){
                    message.setText("На балансе у пользователя сумма меньше");
                    return message;
                }

                accountService.withdraw(telegramUsers.getUserId(), BigDecimal.valueOf(amount));
                message.setText(amount + " Был снять у пользователя с id - " + telegramUsers.getUserId());

                stateManager.deleteUserState(telegramUserID);

                return message;
            } catch (NumberFormatException e) {
                SendMessage message = new SendMessage();
                message.setChatId(chatId);
                message.setText("Ошибка: введите корректную сумму.");

                return message;
            }
        }
        return null;
    }


    @Override
    public boolean canHandle(String stateStatus) {
        String[] parts = stateStatus.split("__");
        if (parts[0].equals("WAITING_FOR_WITHDRAW_RUB_ACCOUNT")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public SendMessage handle(StatePaymentHistory paymentHistory, Long chatId, String messageText, Long telegramUserID) {
        return waitingForWithRubAccount(paymentHistory, chatId, messageText, telegramUserID);
    }
}
