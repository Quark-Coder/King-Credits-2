package com.kingleaks.king_credits.bot.callback;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@RequiredArgsConstructor
public class HeadAndTailsCallback implements CallbackQueryHandler{
    @Override
    public boolean canHandle(String callbackData) {
        return "HEADS_AND_TAILS".equals(callbackData);
    }

    @Override
    public void handle(CallbackQuery callbackQuery) {

    }
}
