package com.kingleaks.king_credits.bot.waitingState;

import com.kingleaks.king_credits.domain.entity.StatePaymentHistory;
import com.kingleaks.king_credits.service.CalculateService;
import com.kingleaks.king_credits.service.StateManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StateWaitingForCreditsInRub implements StateWaitingQueryHandler {
    private final StateManagerService stateManager;
    private final CalculateService calculateService;

    public SendMessage waitingForCreditsInRub(StatePaymentHistory paymentHistory,
                                        Long chatId, String messageText, Long telegramUserID){
        if (paymentHistory != null){
            try {
                double amount = Double.parseDouble(messageText);
                double result = calculateService.calculateCreditsInRub(amount);

                SendMessage message = new SendMessage();
                message.setChatId(chatId);
                message.setText(String.format("%.2f ₽", result));
                message.setReplyMarkup(ReplyKeyboardMarkup.builder()
                        .keyboardRow(new KeyboardRow(List.of(new KeyboardButton("\uD83D\uDCC3 Меню"))))
                        .resizeKeyboard(true).build());

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
        return "WAITING_FOR_CALCULATION_CREDITSRUB".equals(stateStatus);
    }

    @Override
    public SendMessage handle(StatePaymentHistory paymentHistory, Long chatId, String messageText, Long telegramUserID) {
        return waitingForCreditsInRub(paymentHistory, chatId, messageText, telegramUserID);
    }
}
