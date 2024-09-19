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
public class SelectPromoCodeCallback implements CallbackQueryHandler {
    private final BotService botService;
    private final StateManagerService stateManager;

    @Override
    public boolean canHandle(String callbackData) {
        return "SELECT_PROMOCODE".equals(callbackData);
    }

    @Override
    public void handle(CallbackQuery callbackQuery) {
        StatePaymentHistory userState = new StatePaymentHistory();
        userState.setTelegramUserId(callbackQuery.getFrom().getId());
        userState.setStatus("WAITING_FOR_SELECT_ID_PROMOCODE");
        stateManager.setUserState(callbackQuery.getFrom().getId(), userState);

        String result = "Укажите номер промокода:";

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(callbackQuery.getMessage().getChatId());
        sendMessage.setText(result);
        botService.sendMessage(sendMessage);
    }
}
