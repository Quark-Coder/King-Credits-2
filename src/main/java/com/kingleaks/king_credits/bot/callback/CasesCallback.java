package com.kingleaks.king_credits.bot.callback;

import com.kingleaks.king_credits.bot.BotService;
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
public class CasesCallback implements CallbackQueryHandler{
    private final BotService botService;

    @Override
    public boolean canHandle(String callbackData) {
        return "CASES".equals(callbackData);
    }

    @Override
    public void handle(CallbackQuery callbackQuery) {
        deleteMessage(callbackQuery);
        SendMessage message = SendMessage.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .text("Кейсы")
                .build();
        message.setReplyMarkup(ReplyKeyboardMarkup.builder()
                .keyboardRow(new KeyboardRow(List.of(new KeyboardButton("Назад"))))
                .resizeKeyboard(true).build());
        botService.sendMessage(message);

        SendMessage option = new SendMessage();
        option.setChatId(callbackQuery.getMessage().getChatId());
        option.setText("Выберите опцию");

        InlineKeyboardButton buyCases = new InlineKeyboardButton();
        buyCases.setText("Купить кейсы");
        buyCases.setCallbackData("BUY_CASES");
        InlineKeyboardButton myCases = new InlineKeyboardButton();
        myCases.setText("Мои кейсы");
        myCases.setCallbackData("MY_CASES");

        List<InlineKeyboardButton> buttons = List.of(buyCases, myCases);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(List.of(buttons));

        option.setReplyMarkup(markup);
        botService.sendMessage(option);
    }

    private void deleteMessage(CallbackQuery callbackQuery) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(callbackQuery.getMessage().getChatId());
        deleteMessage.setMessageId(callbackQuery.getMessage().getMessageId());
        botService.deleteMessage(deleteMessage);
    }
}
