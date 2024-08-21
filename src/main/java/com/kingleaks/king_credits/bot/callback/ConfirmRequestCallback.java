package com.kingleaks.king_credits.bot.callback;

import com.kingleaks.king_credits.bot.BotService;
import com.kingleaks.king_credits.domain.entity.TelegramUsers;
import com.kingleaks.king_credits.service.ReplenishmentRequestsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@RequiredArgsConstructor
public class ConfirmRequestCallback implements CallbackQueryHandler{
    private final BotService botService;
    private final ReplenishmentRequestsService replenishmentRequestsService;

    @Override
    public boolean canHandle(String callbackData) {
        String[] parts = callbackData.split("_");
        if (parts[0].equals("CONFIRMREQUEST")) {
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
        Long id = Long.parseLong(parts[1]);
        TelegramUsers telegramUsers = replenishmentRequestsService.confirmRequest(id);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Готово, баланс клиента обновлен.");
        botService.sendMessage(sendMessage);

        SendMessage sendMessageForUser = new SendMessage();
        sendMessageForUser.setChatId(telegramUsers.getChatId());
        sendMessageForUser.setText("Готово, ваш баланс пополнен. Можете проверить, зайдя во вкладку \"Профиль\".");
        botService.sendMessage(sendMessageForUser);
    }

    private void deleteMessage(CallbackQuery callbackQuery) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(callbackQuery.getMessage().getChatId());
        deleteMessage.setMessageId(callbackQuery.getMessage().getMessageId());
        botService.deleteMessage(deleteMessage);
    }
}
