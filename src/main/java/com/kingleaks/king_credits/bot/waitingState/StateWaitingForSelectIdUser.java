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
public class StateWaitingForSelectIdUser implements StateWaitingQueryHandler {
    private final TelegramUsersService telegramUsersService;
    private final StateManagerService stateManager;

    public SendMessage waitingForSelectIdUser(StatePaymentHistory paymentHistory,
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
            InlineKeyboardButton withdraw = new InlineKeyboardButton();
            withdraw.setText("Снять рубли");
            withdraw.setCallbackData("WITHDRAW_RUB__" + selectId);
            InlineKeyboardButton give = new InlineKeyboardButton();
            give.setText("Выдать рубли");
            give.setCallbackData("GIVE_RUB__" + selectId);
            InlineKeyboardButton ban = new InlineKeyboardButton();
            ban.setText("БАН");
            ban.setCallbackData("BAN__" + selectId);

            List<InlineKeyboardButton> button1 = List.of(withdraw, give);
            List<InlineKeyboardButton> button2 = List.of(ban);
            InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
            markup.setKeyboard(List.of(button1, button2));

            message.setReplyMarkup(markup);

            stateManager.deleteUserState(telegramUserID);
            return message;
        }
        return null;
    }
    @Override
    public boolean canHandle(String stateStatus) {
        return "WAITING_FOR_SELECT_ID_USER".equals(stateStatus);
    }

    @Override
    public SendMessage handle(StatePaymentHistory paymentHistory, Long chatId, String messageText, Long telegramUserID) {
        return waitingForSelectIdUser(paymentHistory, chatId, messageText, telegramUserID);
    }
}
