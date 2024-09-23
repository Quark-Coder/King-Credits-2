package com.kingleaks.king_credits.bot.waitingState;

import com.kingleaks.king_credits.domain.entity.StatePaymentHistory;
import com.kingleaks.king_credits.repository.CasesItemRepository;
import com.kingleaks.king_credits.repository.CasesRepository;
import com.kingleaks.king_credits.service.StateManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
@RequiredArgsConstructor
public class StateWaitingForSelectCasesWithoutImage implements StateWaitingQueryHandler {
    private final CasesRepository casesRepository;
    private final StateManagerService stateManager;

    public SendMessage waitingForSelectCasesWithoutImage(StatePaymentHistory paymentHistory,
                                                        Long chatId, String messageText, Long telegramUserID){
        if (paymentHistory != null){
            Long selectId = Long.parseLong(messageText);
            if (casesRepository.findById(selectId).isEmpty()){
                SendMessage message = new SendMessage();
                message.setChatId(chatId);
                message.setText("Укажите правильно номер кейса");
                return message;
            }

            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText("Отправьте фотографию кейса");

            paymentHistory.setStatus("WAITING_FOR_CASES_PHOTO__"+selectId);
            stateManager.setUserState(telegramUserID, paymentHistory);
            return message;
        }
        return null;
    }

    @Override
    public boolean canHandle(String stateStatus) {
        return "WAITING_FOR_SELECT_CASES_WITHOUT_IMAGE".equals(stateStatus);
    }

    @Override
    public SendMessage handle(StatePaymentHistory paymentHistory, Long chatId, String messageText, Long telegramUserID) {
        return waitingForSelectCasesWithoutImage(paymentHistory, chatId, messageText, telegramUserID);
    }
}
