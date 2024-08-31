package com.kingleaks.king_credits.bot.waitingState;

import com.kingleaks.king_credits.domain.entity.StatePaymentHistory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface StateWaitingQueryHandler {
    boolean canHandle(String stateStatus);
    SendMessage handle(StatePaymentHistory paymentHistory,
                       Long chatId, String messageText, Long telegramUserID);
}
