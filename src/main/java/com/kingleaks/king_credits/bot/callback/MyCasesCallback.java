package com.kingleaks.king_credits.bot.callback;

import com.kingleaks.king_credits.bot.BotService;
import com.kingleaks.king_credits.service.CasesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MyCasesCallback implements CallbackQueryHandler {
    private final BotService botService;
    private final CasesService casesService;

    @Override
    public boolean canHandle(String callbackData) {
        return "MY_CASES".equals(callbackData);
    }

    @Override
    public void handle(CallbackQuery callbackQuery) {
        deleteMessage(callbackQuery);
        Long telegramUserId = callbackQuery.getFrom().getId();

        SendMessage message = SendMessage.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .text("Мои кейсы")
                .build();
        message.setReplyMarkup(ReplyKeyboardMarkup.builder()
                .keyboardRow(new KeyboardRow(List.of(new KeyboardButton("Назад")))).build());
        botService.sendMessage(message);

        String result = casesService.getAllCasesUser(telegramUserId);

        SendMessage inventory = new SendMessage();
        inventory.setChatId(callbackQuery.getMessage().getChatId());

        if (!result.isEmpty()) {
            InlineKeyboardButton selectCaseInventory = new InlineKeyboardButton();
            selectCaseInventory.setText("Выбрать кейс");
            selectCaseInventory.setCallbackData("SELECT_CASE_INVENTORY");

            List<InlineKeyboardButton> buttons = List.of(selectCaseInventory);
            InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
            markup.setKeyboard(List.of(buttons));

            inventory.setText(result);
            inventory.setReplyMarkup(markup);
        } else {
            inventory.setText("Список пуст");
        }

        botService.sendMessage(inventory);

    }

    private void deleteMessage(CallbackQuery callbackQuery) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(callbackQuery.getMessage().getChatId());
        deleteMessage.setMessageId(callbackQuery.getMessage().getMessageId());
        botService.deleteMessage(deleteMessage);
    }
}
