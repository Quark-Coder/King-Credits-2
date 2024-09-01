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
public class SelectCaseCallback implements CallbackQueryHandler{
    private final StateManagerService stateManager;
    private final BotService botService;

    @Override
    public boolean canHandle(String callbackData) {
        return "SELECT_CASE".equals(callbackData);
    }

    @Override
    public void handle(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        Long telegramUserId = callbackQuery.getFrom().getId();

        StatePaymentHistory userState = new StatePaymentHistory();
        userState.setTelegramUserId(telegramUserId);
        userState.setStatus("WAITING_FOR_SELECT_CASE");
        stateManager.setUserState(telegramUserId, userState);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Для покупки просто укажите номер кейса, например - 3");
        botService.sendMessage(sendMessage);
    }
}
