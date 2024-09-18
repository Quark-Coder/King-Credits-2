package com.kingleaks.king_credits.bot.waitingState;

import com.kingleaks.king_credits.domain.entity.PromoCode;
import com.kingleaks.king_credits.domain.entity.StatePaymentHistory;
import com.kingleaks.king_credits.service.PromoCodeService;
import com.kingleaks.king_credits.service.StateManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class StateWaitingForEnterPromoCode implements StateWaitingQueryHandler{
    private final PromoCodeService promoCodeService;
    private final StateManagerService stateManager;

    public SendMessage waitingForEnterPromoCode(StatePaymentHistory paymentHistory,
                                                 Long chatId, String messageText, Long telegramUserID){
        if (paymentHistory != null){
            String code = promoCodeService.enterPromoCode(messageText, telegramUserID);

            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText(code);

            stateManager.deleteUserState(telegramUserID);

            return message;
        }
        return null;
    }


    @Override
    public boolean canHandle(String stateStatus) {
        return "WAITING_FOR_ENTER_PROMOCODE".equals(stateStatus);
    }

    @Override
    public SendMessage handle(StatePaymentHistory paymentHistory, Long chatId, String messageText, Long telegramUserID) {
        return waitingForEnterPromoCode(paymentHistory, chatId, messageText, telegramUserID);
    }
}
