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
public class StateWaitingForWithdrawalOfCredits implements StateWaitingQueryHandler {
    private final StateManagerService stateManager;
    private final WithdrawalOfCreditsService withdrawalOfCreditsService;

    public SendMessage waitingForWithdrawalOfCredits(StatePaymentHistory paymentHistory,
                                              Long chatId, String messageText, Long telegramUserID){
        if (paymentHistory != null){
            try {
                double amount = Double.parseDouble(messageText);
                if (amount < 100){
                    SendMessage message = new SendMessage();
                    message.setChatId(chatId);
                    message.setText("Сумма кредитов должна быть выше 100");
                    message.setReplyMarkup(ReplyKeyboardMarkup.builder()
                            .keyboardRow(new KeyboardRow(List.of(new KeyboardButton("\uD83D\uDCC3 Меню"))))
                            .resizeKeyboard(true).build());
                    return message;
                } else {
                    String result = withdrawalOfCreditsService
                            .createWithdrawalOfCredits(telegramUserID, amount, paymentHistory);

                    //логика списывания денег
                    SendMessage message = new SendMessage();
                    message.setChatId(chatId);
                    message.setText(result);
                    message.setReplyMarkup(ReplyKeyboardMarkup.builder()
                            .keyboardRow(new KeyboardRow(List.of(new KeyboardButton("\uD83D\uDCC3 Меню"))))
                            .resizeKeyboard(true).build());

                    return message;
                }
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
        return "WAITING_FOR_AMOUNT_FOR_WITHDRAWAL".equals(stateStatus);
    }

    @Override
    public SendMessage handle(StatePaymentHistory paymentHistory, Long chatId, String messageText, Long telegramUserID) {
        return waitingForWithdrawalOfCredits(paymentHistory, chatId, messageText, telegramUserID);
    }
}
