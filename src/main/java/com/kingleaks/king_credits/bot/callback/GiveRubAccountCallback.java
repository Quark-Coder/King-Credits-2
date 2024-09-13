package com.kingleaks.king_credits.bot.callback;

import com.kingleaks.king_credits.bot.BotService;
import com.kingleaks.king_credits.domain.entity.StatePaymentHistory;
import com.kingleaks.king_credits.service.StateManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@RequiredArgsConstructor
public class GiveRubAccountCallback implements CallbackQueryHandler {
    private final BotService botService;
    private final StateManagerService stateManager;

    @Override
    public boolean canHandle(String callbackData) {
        String[] parts = callbackData.split("__");
        if (parts[0].equals("GIVE_RUB_ACCOUNT")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void handle(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        String[] parts = callbackQuery.getData().split("__");
        Long userId = Long.parseLong(parts[1]);

        StatePaymentHistory userState = new StatePaymentHistory();
        userState.setTelegramUserId(callbackQuery.getFrom().getId());
        userState.setStatus("WAITING_FOR_GIVE_RUB_ACCOUNT__"+userId);
        stateManager.setUserState(callbackQuery.getFrom().getId(), userState);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Напишите суммы которую вы хотите выдать пользователю");
        botService.sendMessage(sendMessage);
    }
}
