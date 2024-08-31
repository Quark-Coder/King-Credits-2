package com.kingleaks.king_credits.bot.waitingState;

import com.kingleaks.king_credits.domain.entity.StatePaymentHistory;
import com.kingleaks.king_credits.service.StateManagerService;
import com.kingleaks.king_credits.service.TelegramUsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
@RequiredArgsConstructor
public class StateWaitingForChangeNickname implements StateWaitingQueryHandler {
    private final TelegramUsersService telegramUsersService;
    private final StateManagerService stateManager;

    public SendMessage waitingForChangeNickname(StatePaymentHistory paymentHistory,
                                        Long chatId, String messageText, Long telegramUserID){
        if (paymentHistory != null){
            telegramUsersService.changeNickname(telegramUserID, messageText);

            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText("Отлично! Ваш никнейм изменен в профиле.");


            stateManager.deleteUserState(telegramUserID);

            return message;
        }

        return null;
    }

    @Override
    public boolean canHandle(String stateStatus) {
        return "WAITING_FOR_CHANGE_NICKNAME".equals(stateStatus);
    }

    @Override
    public SendMessage handle(StatePaymentHistory paymentHistory, Long chatId, String messageText, Long telegramUserID) {
        return waitingForChangeNickname(paymentHistory, chatId, messageText, telegramUserID);
    }
}
