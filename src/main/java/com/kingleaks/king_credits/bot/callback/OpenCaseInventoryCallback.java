package com.kingleaks.king_credits.bot.callback;

import com.kingleaks.king_credits.bot.BotService;
import com.kingleaks.king_credits.domain.entity.CasesItem;
import com.kingleaks.king_credits.service.AccountService;
import com.kingleaks.king_credits.service.CasesItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OpenCaseInventoryCallback implements CallbackQueryHandler{
    private final BotService botService;
    private final CasesItemService casesItemService;
    private final AccountService accountService;

    @Override
    public boolean canHandle(String callbackData) {
        String[] parts = callbackData.split("__");
        if (parts[0].equals("OPEN_CASE_INVENTORY")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void handle(CallbackQuery callbackQuery) {
        deleteMessage(callbackQuery);

        Long chatId = callbackQuery.getMessage().getChatId();
        String[] parts = callbackQuery.getData().split("__");
        Long telegramUserId = callbackQuery.getFrom().getId();
        Long inventoryId = Long.parseLong(parts[1]);

        CasesItem item = casesItemService.getRandomItem(inventoryId);
        accountService.replenish(telegramUserId, BigDecimal.valueOf(item.getPrice()));

        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text("Вот твой дроп! Поздравляем с выигрышем.\n" +
                        item.getName() + " Стоимость - " + item.getPrice())
                .build();
        message.setReplyMarkup(ReplyKeyboardMarkup.builder()
                .keyboardRow(new KeyboardRow(List.of(new KeyboardButton("Назад")))).build());

        botService.sendMessage(message);
    }

    private void deleteMessage(CallbackQuery callbackQuery) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(callbackQuery.getMessage().getChatId());
        deleteMessage.setMessageId(callbackQuery.getMessage().getMessageId());
        botService.deleteMessage(deleteMessage);
    }
}
