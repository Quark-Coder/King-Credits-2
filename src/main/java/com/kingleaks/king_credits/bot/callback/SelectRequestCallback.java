package com.kingleaks.king_credits.bot.callback;

import com.kingleaks.king_credits.bot.BotService;
import com.kingleaks.king_credits.domain.entity.StatePaymentHistory;
import com.kingleaks.king_credits.service.StateManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SelectRequestCallback implements CallbackQueryHandler{
    private final BotService botService;
    private final StateManagerService stateManager;

    @Override
    public boolean canHandle(String callbackData) {
        return "SELECT_REQUEST".equals(callbackData);
    }

    @Override
    public void handle(CallbackQuery callbackQuery) {
        //deleteMessage(callbackQuery);
        Long chatId = callbackQuery.getMessage().getChatId();
        Long telegramUserId = callbackQuery.getFrom().getId();

        StatePaymentHistory userState = new StatePaymentHistory();
        userState.setTelegramUserId(telegramUserId);
        userState.setStatus("WAITING_FOR_SELECT_REQUEST");
        stateManager.setUserState(telegramUserId, userState);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Укажите номер чека для выбора заявки");
        botService.sendMessage(sendMessage);
    }

    /*private void deleteMessage(CallbackQuery callbackQuery) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(callbackQuery.getMessage().getChatId());
        deleteMessage.setMessageId(callbackQuery.getMessage().getMessageId());
        botService.deleteMessage(deleteMessage);
    }*/
}
