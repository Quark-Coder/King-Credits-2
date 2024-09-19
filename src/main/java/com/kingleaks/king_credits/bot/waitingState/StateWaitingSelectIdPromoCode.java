package com.kingleaks.king_credits.bot.waitingState;

import com.kingleaks.king_credits.domain.entity.StatePaymentHistory;
import com.kingleaks.king_credits.service.PromoCodeService;
import com.kingleaks.king_credits.service.StateManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StateWaitingSelectIdPromoCode implements StateWaitingQueryHandler{
    private final StateManagerService stateManager;
    private final PromoCodeService promoCodeService;

    public SendMessage waitingForSelectIdPromoCode(StatePaymentHistory paymentHistory,
                                                 Long chatId, String messageText, Long telegramUserID){
        if (paymentHistory != null){
            try {
                Long selectId = Long.parseLong(messageText);
                String result = promoCodeService.getPromoCodeById(selectId);
                SendMessage message = new SendMessage();
                message.setChatId(chatId);

                if (result != null){
                    message.setText(result);

                    InlineKeyboardButton buyThisCase = new InlineKeyboardButton();
                    buyThisCase.setText("Удалить промокод");
                    buyThisCase.setCallbackData("DELETE_PROMOCODE__"+selectId);

                    List<InlineKeyboardButton> buttons = List.of(buyThisCase);
                    InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
                    markup.setKeyboard(List.of(buttons));
                    message.setReplyMarkup(markup);

                    stateManager.deleteUserState(telegramUserID);
                    return message;
                } else {
                    message.setText("Вы не правильно указали номер промокода, отправьте корректный номер промокода например 1");
                    return message;
                }
            } catch (NumberFormatException e) {
                SendMessage message = new SendMessage();
                message.setChatId(chatId);
                message.setText("Ошибка: введите корректную сумму.");

                return message;
            }
        }
        return null;
    }

    @Override
    public boolean canHandle(String stateStatus) {
        return "WAITING_FOR_SELECT_ID_PROMOCODE".equals(stateStatus);
    }

    @Override
    public SendMessage handle(StatePaymentHistory paymentHistory, Long chatId, String messageText, Long telegramUserID) {
        return waitingForSelectIdPromoCode(paymentHistory, chatId, messageText, telegramUserID);
    }
}
