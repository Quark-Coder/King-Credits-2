package com.kingleaks.king_credits.bot.waitingState;

import com.kingleaks.king_credits.domain.entity.StatePaymentHistory;
import com.kingleaks.king_credits.service.StateManagerService;
import com.kingleaks.king_credits.service.TelegramUsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StateWaitingForSelectIdBanned implements StateWaitingQueryHandler{
    private final TelegramUsersService telegramUsersService;
    private final StateManagerService stateManager;

    public SendMessage waitingForSelectIdBanned(StatePaymentHistory paymentHistory,
                                              Long chatId, String messageText, Long telegramUserID){
        if (paymentHistory != null) {
            Long selectId = Long.parseLong(messageText);
            String result = telegramUsersService.getInformationUserProfileForAdmin(selectId);

            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            if (result == null) {
                message.setText("Вы не правильно указали id пользователя");
                return message;
            }

            message.setText(result);
            InlineKeyboardButton ban = new InlineKeyboardButton();
            ban.setText("РАЗБАНИТЬ");
            ban.setCallbackData("UNBAN_" + selectId);

            List<InlineKeyboardButton> button2 = List.of(ban);
            InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
            markup.setKeyboard(List.of(button2));

            message.setReplyMarkup(markup);

            stateManager.deleteUserState(telegramUserID);
            return message;
        }
        return null;
    }
    @Override
    public boolean canHandle(String stateStatus) {
        return "WAITING_FOR_SELECT_ID_BANNED".equals(stateStatus);
    }

    @Override
    public SendMessage handle(StatePaymentHistory paymentHistory, Long chatId, String messageText, Long telegramUserID) {
        return waitingForSelectIdBanned(paymentHistory, chatId, messageText, telegramUserID);
    }
}
