package com.kingleaks.king_credits.bot.callback;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public class MyCasesCallback implements CallbackQueryHandler {
    @Override
    public boolean canHandle(String callbackData) {
        return "MY_CASES".equals(callbackData);
    }

    @Override
    public void handle(CallbackQuery callbackQuery) {

    }
}
