package com.kingleaks.king_credits.bot.callback;

import com.kingleaks.king_credits.bot.BotService;
import com.kingleaks.king_credits.domain.entity.StatePaymentHistory;
import com.kingleaks.king_credits.service.StateManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@RequiredArgsConstructor
public class CreatePromoCodeCallback implements CallbackQueryHandler{
    private final BotService botService;
    private final StateManagerService stateManager;

    @Override
    public boolean canHandle(String callbackData) {
        return "CREATE_PROMOCODE".equals(callbackData);
    }

    @Override
    public void handle(CallbackQuery callbackQuery) {
        deleteMessage(callbackQuery);
        Long chatId = callbackQuery.getMessage().getChatId();

        StatePaymentHistory userState = new StatePaymentHistory();
        userState.setTelegramUserId(callbackQuery.getFrom().getId());
        userState.setStatus("WAITING_FOR_CREATE_PROMOCODE");
        stateManager.setUserState(callbackQuery.getFrom().getId(), userState);

        String result = "Укажите следующие данные \n" +
                "1 - код \n" +
                "2 - дату окончания действия \n" +
                "3 - количество применений \n" +
                "4 - сумму награды в рублях \n" +
                "Пример - QWERTY_31.10.2024_5_100\n" +
                "ВАЖНО соблюдать формат поля!";

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(result);
        botService.sendMessage(sendMessage);
    }

    private void deleteMessage(CallbackQuery callbackQuery) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(callbackQuery.getMessage().getChatId());
        deleteMessage.setMessageId(callbackQuery.getMessage().getMessageId());
        botService.deleteMessage(deleteMessage);
    }
}
