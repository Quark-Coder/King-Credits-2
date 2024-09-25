package com.kingleaks.king_credits.bot.waitingState;

import com.kingleaks.king_credits.domain.entity.StatePaymentHistory;
import com.kingleaks.king_credits.repository.CasesRepository;
import com.kingleaks.king_credits.repository.StateImageRepository;
import com.kingleaks.king_credits.service.StateImageService;
import com.kingleaks.king_credits.service.StateManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StateWaitingForSelectStateImage implements StateWaitingQueryHandler{
    private final StateImageRepository stateImageRepository;
    private final StateImageService stateImageService;
    private final StateManagerService stateManager;

    public SendMessage waitingForSelectStateImage(StatePaymentHistory paymentHistory,
                                                         Long chatId, String messageText, Long telegramUserID){
        if (paymentHistory != null) {
            stateManager.deleteUserState(telegramUserID);
            Long selectId = Long.parseLong(messageText);
            String result = stateImageService.getInformationState(selectId);

            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            if (result == null) {
                message.setText("Вы не правильно указали id раздела");
                return message;
            }

            message.setText(result);
            InlineKeyboardButton operation = new InlineKeyboardButton();
            if (stateImageRepository.isStateImageHasPicture(selectId)){
                operation.setText("Загрузить изображения раздела");
                operation.setCallbackData("UPLOAD_STATE_IMAGE__" + selectId);
            } else {
                operation.setText("Удалить изображения раздела");
                operation.setCallbackData("DELETE_STATE_IMAGE__" + selectId);
            }

            List<InlineKeyboardButton> button1 = List.of(operation);
            InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
            markup.setKeyboard(List.of(button1));

            message.setReplyMarkup(markup);
            return message;
        }
        return null;
    }

    @Override
    public boolean canHandle(String stateStatus) {
        return "WAITING_FOR_SELECT_STATE_IMAGE".equals(stateStatus);
    }

    @Override
    public SendMessage handle(StatePaymentHistory paymentHistory, Long chatId, String messageText, Long telegramUserID) {
        return waitingForSelectStateImage(paymentHistory, chatId, messageText, telegramUserID);
    }
}
