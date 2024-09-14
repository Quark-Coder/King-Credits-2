package com.kingleaks.king_credits.bot.callback;

import com.kingleaks.king_credits.bot.BotService;
import com.kingleaks.king_credits.service.TelegramUsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@RequiredArgsConstructor
public class PeriodCallback implements CallbackQueryHandler {
    private final BotService botService;
    private final TelegramUsersService telegramUsersService;

    @Override
    public boolean canHandle(String callbackData) {
        String[] parts = callbackData.split("__");
        if (parts[0].equals("PERIOD")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void handle(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        String[] parts = callbackQuery.getData().split("__");

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        if (parts[1].equals("ALL_TIME")){
            sendMessage.setText(telegramUsersService.getStatisticForAllTime());
        } else {
            int period = Integer.parseInt(parts[1]);
            sendMessage.setText(telegramUsersService.getStatisticForPeriod(period));
        }

        botService.sendMessage(sendMessage);
    }
}
