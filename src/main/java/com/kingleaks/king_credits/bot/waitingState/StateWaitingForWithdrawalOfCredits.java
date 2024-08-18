package com.kingleaks.king_credits.bot.waitingState;

import com.kingleaks.king_credits.domain.entity.StatePaymentHistory;
import com.kingleaks.king_credits.service.CalculateService;
import com.kingleaks.king_credits.service.StateManagerService;
import com.kingleaks.king_credits.service.WithdrawalOfCreditsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StateWaitingForWithdrawalOfCredits {
    private final StateManagerService stateManager;
    private final WithdrawalOfCreditsService withdrawalOfCreditsService;

    public SendMessage waitingForWithdrawalOfCredits(StatePaymentHistory paymentHistory,
                                              Long chatId, String messageText, Long telegramUserID){
        if (paymentHistory != null){
            try {
                double amount = Double.parseDouble(messageText);
                withdrawalOfCreditsService.createWithdrawalOfCredits(telegramUserID, amount);

                //логика списывания денег
                SendMessage message = new SendMessage();
                message.setChatId(chatId);
                message.setText("Хорошо, деньги списаны с вашего баланса." +
                        " Выставьте любой скин за " + amount + " (формула для подсчета, с учетом коммисии рынка " +
                        "- сумма разделить на 0.8). Пришлите скрин скина с рынка. ");
                message.setReplyMarkup(ReplyKeyboardMarkup.builder()
                        .keyboardRow(new KeyboardRow(List.of(new KeyboardButton("Меню")))).build());

                paymentHistory.setStatus("WAITING_FOR_AMOUNT_WITHDRAWAL_PHOTO");
                stateManager.setUserState(telegramUserID, paymentHistory);

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
}
