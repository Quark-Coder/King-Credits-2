package com.kingleaks.king_credits.bot.callback;

import com.kingleaks.king_credits.bot.CallbackQueryHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
public class TopUpBalanceCallback implements CallbackQueryHandler {

    @Override
    public boolean canHandle(String callbackData) {
        return "SUBMIT_BUTTON".equals(callbackData);
    }

    @Override
    public void handle(CallbackQuery callbackQuery) {
        System.out.println("Блять нахуй я вообще сюда пришел");
    }
}
