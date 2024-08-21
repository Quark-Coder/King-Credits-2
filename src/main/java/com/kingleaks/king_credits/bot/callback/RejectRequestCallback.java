package com.kingleaks.king_credits.bot.callback;

import com.kingleaks.king_credits.bot.BotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@RequiredArgsConstructor
public class RejectRequestCallback implements CallbackQueryHandler{
    private final BotService botService;

    @Override
    public boolean canHandle(String callbackData) {
        String[] parts = callbackData.split("_");
        if (parts[0].equals("REJECTREQUEST")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void handle(CallbackQuery callbackQuery) {
        deleteMessage(callbackQuery);
        System.out.println("Работает удалить");
    }

    private void deleteMessage(CallbackQuery callbackQuery) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(callbackQuery.getMessage().getChatId());
        deleteMessage.setMessageId(callbackQuery.getMessage().getMessageId());
        botService.deleteMessage(deleteMessage);
    }
}
