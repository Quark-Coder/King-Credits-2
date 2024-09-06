package com.kingleaks.king_credits.bot.waitingState;

import com.kingleaks.king_credits.domain.entity.StatePaymentHistory;
import com.kingleaks.king_credits.repository.CasesItemRepository;
import com.kingleaks.king_credits.service.StateManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
@RequiredArgsConstructor
public class StateWaitingForSelectItemWithoutImage implements StateWaitingQueryHandler{
    private final CasesItemRepository casesItemRepository;
    private final StateManagerService stateManager;

    public SendMessage waitingForSelectItemWithoutImage(StatePaymentHistory paymentHistory,
                                              Long chatId, String messageText, Long telegramUserID){
        if (paymentHistory != null){
            Long selectId = Long.parseLong(messageText);
            if (casesItemRepository.findById(selectId).isEmpty()){
                SendMessage message = new SendMessage();
                message.setChatId(chatId);
                message.setText("Укажите правильно номер предмета");
                return message;
            }

            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText("Отправьте фотографию дропа");

            paymentHistory.setStatus("WAITING_FOR_ITEM_PHOTO__"+selectId);
            stateManager.setUserState(telegramUserID, paymentHistory);
            return message;
        }
        return null;
    }

    @Override
    public boolean canHandle(String stateStatus) {
        return "WAITING_FOR_SELECT_ITEM_WITHOUT_IMAGE".equals(stateStatus);
    }

    @Override
    public SendMessage handle(StatePaymentHistory paymentHistory, Long chatId, String messageText, Long telegramUserID) {
        return waitingForSelectItemWithoutImage(paymentHistory, chatId, messageText, telegramUserID);
    }
}
