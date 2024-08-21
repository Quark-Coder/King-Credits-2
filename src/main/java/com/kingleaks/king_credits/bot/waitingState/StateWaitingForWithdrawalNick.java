package com.kingleaks.king_credits.bot.waitingState;

import com.kingleaks.king_credits.domain.entity.StatePaymentHistory;
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
public class StateWaitingForWithdrawalNick {
    private final StateManagerService stateManager;
    private final WithdrawalOfCreditsService withdrawalOfCreditsService;

    public SendMessage waitingForWithdrawalNick(StatePaymentHistory paymentHistory,
                                                     Long chatId, String messageText, Long telegramUserID){
        if (paymentHistory != null){
            //Тут должна быть логика сохранения информации о никнейме
            withdrawalOfCreditsService.initNickInGameWithdrawalOfCredits(telegramUserID, messageText);
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText("Отлично! Оплата прошла успешно. Ожидайте, мы купим ваш скин в течение дня и уведомим вас об этом!");
            message.setReplyMarkup(ReplyKeyboardMarkup.builder()
                    .keyboardRow(new KeyboardRow(List.of(new KeyboardButton("Меню")))).build());

            stateManager.deleteUserState(telegramUserID);
            return message;

        }
        return null;
    }
}