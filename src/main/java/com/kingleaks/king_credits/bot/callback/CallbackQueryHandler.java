package com.kingleaks.king_credits.bot.callback;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public interface CallbackQueryHandler {
    boolean canHandle(String callbackData);
    void handle(CallbackQuery callbackQuery);
}
