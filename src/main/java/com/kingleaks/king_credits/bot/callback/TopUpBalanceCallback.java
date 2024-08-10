package com.kingleaks.king_credits.bot.callback;

import com.kingleaks.king_credits.bot.BotService;
import com.kingleaks.king_credits.bot.CallbackQueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@RequiredArgsConstructor
public class TopUpBalanceCallback implements CallbackQueryHandler {
    private final BotService botService;

    @Override
    public boolean canHandle(String callbackData) {
        return "TOP_UP_BUTTON".equals(callbackData);
    }

    @Override
    public void handle(CallbackQuery callbackQuery) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(callbackQuery.getMessage().getChatId());
        deleteMessage.setMessageId(callbackQuery.getMessage().getMessageId());
        System.out.println("Блять нахуй я вообще сюда пришел");
        botService.deleteMessage(deleteMessage);
    }
}
