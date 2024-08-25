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
                String result = withdrawalOfCreditsService
                        .createWithdrawalOfCredits(telegramUserID, amount, paymentHistory);

                //логика списывания денег
                SendMessage message = new SendMessage();
                message.setChatId(chatId);
                message.setText(result);
                message.setReplyMarkup(ReplyKeyboardMarkup.builder()
                        .keyboardRow(new KeyboardRow(List.of(new KeyboardButton("Меню")))).build());

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
