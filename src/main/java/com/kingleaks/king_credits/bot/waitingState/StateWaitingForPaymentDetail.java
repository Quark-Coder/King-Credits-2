package com.kingleaks.king_credits.bot.waitingState;

import com.kingleaks.king_credits.domain.entity.StatePaymentHistory;
import com.kingleaks.king_credits.service.StateManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
@RequiredArgsConstructor
public class StateWaitingForPaymentDetail implements StateWaitingQueryHandler{
    private final StateManagerService stateManager;

    public SendMessage waitingForPaymentDetail(StatePaymentHistory paymentHistory,
                                                         Long chatId, String messageText, Long telegramUserID){
        if (paymentHistory != null){
            try {
                String[] parts = messageText.split("_");
                String bankDetail = parts[0];
                String cardNumber = parts[1];

                SendMessage message = new SendMessage();
                message.setChatId(chatId);
                message.setText("Отлично! "+bankDetail+" "+cardNumber+" \n\nТеперь введите ссылку на другой источник оплаты");

                paymentHistory.setStatus("WAITING_FOR_OTHER_PAYMENT__"+messageText);
                stateManager.setUserState(telegramUserID, paymentHistory);
                return message;
            } catch (ArrayIndexOutOfBoundsException e){
                SendMessage message = new SendMessage();
                message.setChatId(chatId);
                message.setText("не правильный формат. Пример: банк_29389283982832");
                return message;
            }
        }
        return null;
    }

    @Override
    public boolean canHandle(String stateStatus) {
        return "WAITING_FOR_PAYMENT_DETAILS".equals(stateStatus);
    }

    @Override
    public SendMessage handle(StatePaymentHistory paymentHistory, Long chatId, String messageText, Long telegramUserID) {
        return waitingForPaymentDetail(paymentHistory, chatId, messageText, telegramUserID);
    }
}
