package com.kingleaks.king_credits.bot.callback;

import com.kingleaks.king_credits.bot.BotService;
import com.kingleaks.king_credits.service.TelegramUsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@RequiredArgsConstructor
public class BanCallback implements CallbackQueryHandler{
    private final BotService botService;
    private final TelegramUsersService telegramUsersService;

    @Override
    public boolean canHandle(String callbackData) {
        String[] parts = callbackData.split("_");
        if (parts[0].equals("BAN")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void handle(CallbackQuery callbackQuery) {
        deleteMessage(callbackQuery);
        Long chatId = callbackQuery.getMessage().getChatId();
        String[] parts = callbackQuery.getData().split("_");
        Long userId = Long.parseLong(parts[1]);

        telegramUsersService.banUser(userId);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Пользователь забанен");
        botService.sendMessage(sendMessage);
    }

    private void deleteMessage(CallbackQuery callbackQuery) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(callbackQuery.getMessage().getChatId());
        deleteMessage.setMessageId(callbackQuery.getMessage().getMessageId());
        botService.deleteMessage(deleteMessage);
    }
}
