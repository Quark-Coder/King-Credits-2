package com.kingleaks.king_credits.bot.waitingState;

import com.kingleaks.king_credits.bot.BotService;
import com.kingleaks.king_credits.domain.entity.StatePaymentHistory;
import com.kingleaks.king_credits.service.PaymentCheckPhotoService;
import com.kingleaks.king_credits.service.StateManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
@RequiredArgsConstructor
public class StateWaitingForAmount {
    private final PaymentCheckPhotoService paymentCheckPhotoService;
    private final StateManagerService stateManager;

    public SendMessage waitingForAmount(StatePaymentHistory paymentHistory,
                                       Long chatId, String messageText, Long telegramUserID){
        if (paymentHistory != null){
            try {
                double amount = Double.parseDouble(messageText);
                paymentCheckPhotoService.createPaymentCheckPhoto(telegramUserID, amount);

                SendMessage message = new SendMessage();
                message.setChatId(chatId);
                message.setText("Хорошо, завершите оплату, любым из способов ниже в течение 10 минут. После оплаты пришлите чек!");

                paymentHistory.setStatus("WAITING_FOR_PAYMENT_CHECK");
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
