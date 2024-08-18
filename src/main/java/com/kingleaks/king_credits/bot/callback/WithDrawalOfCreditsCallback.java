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
public class WithDrawalOfCreditsCallback implements CallbackQueryHandler{
    private final BotService botService;
    private final StateManagerService stateManager;

    @Override
    public boolean canHandle(String callbackData) {
        return "WITH_DRAWAL_OF_CREDITS".equals(callbackData);
    }

    @Override
    public void handle(CallbackQuery callbackQuery) {
        deleteMessage(callbackQuery);
        Long chatId = callbackQuery.getMessage().getChatId();

        StatePaymentHistory userState = new StatePaymentHistory();
        userState.setTelegramUserId(callbackQuery.getFrom().getId());
        userState.setStatus("WAITING_FOR_AMOUNT_FOR_WITHDRAWAL");
        stateManager.setUserState(callbackQuery.getFrom().getId(), userState);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Для вывода кредитов, введите количество кредитов," +
                " которое вы хотите получить. \n(Минимальный вывод 100 кредитов)");
        botService.sendMessage(sendMessage);
    }


    private void deleteMessage(CallbackQuery callbackQuery) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(callbackQuery.getMessage().getChatId());
        deleteMessage.setMessageId(callbackQuery.getMessage().getMessageId());
        botService.deleteMessage(deleteMessage);
    }
}
