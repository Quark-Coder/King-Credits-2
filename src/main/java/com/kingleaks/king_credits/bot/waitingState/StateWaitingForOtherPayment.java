package com.kingleaks.king_credits.bot.waitingState;

import com.kingleaks.king_credits.domain.entity.StatePaymentHistory;
import com.kingleaks.king_credits.service.PaymentDetailsService;
import com.kingleaks.king_credits.service.StateManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
@RequiredArgsConstructor
public class StateWaitingForOtherPayment implements StateWaitingQueryHandler{
    private final StateManagerService stateManager;
    private final PaymentDetailsService paymentDetailsService;

    public SendMessage waitingForOtherPayment(StatePaymentHistory paymentHistory,
                                               Long chatId, String messageText, Long telegramUserID){
        if (paymentHistory != null){
            String[] parts = paymentHistory.getStatus().split("__");
            String paymentDetails = parts[1];

            String[] partsPayment = paymentDetails.split("_");

            String bankDetail = partsPayment[0];
            String cardNumber = partsPayment[1];

            stateManager.deleteUserState(telegramUserID);
            paymentDetailsService.save(bankDetail, cardNumber, messageText);

            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText("Отлично! "+bankDetail+"\n"+cardNumber+"\n"+messageText);

            return message;
        }
        return null;
    }

    @Override
    public boolean canHandle(String stateStatus) {
        String[] parts = stateStatus.split("__");
        if (parts[0].equals("WAITING_FOR_OTHER_PAYMENT")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public SendMessage handle(StatePaymentHistory paymentHistory, Long chatId, String messageText, Long telegramUserID) {
        return waitingForOtherPayment(paymentHistory, chatId, messageText, telegramUserID);
    }
}
