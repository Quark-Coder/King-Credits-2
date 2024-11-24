package com.kingleaks.king_credits.bot.callback;

import com.kingleaks.king_credits.bot.BotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Component
@RequiredArgsConstructor
public class HeadAndTailsCallback implements CallbackQueryHandler{
    private final BotService botService;

    @Override
    public boolean canHandle(String callbackData) {
        return "HEADS_AND_TAILS".equals(callbackData);
    }

    @Override
    public void handle(CallbackQuery callbackQuery) {
        deleteMessage(callbackQuery);
        SendMessage option = new SendMessage();
        option.setChatId(callbackQuery.getMessage().getChatId());
        option.setText("\uD83E\uDE99 Это игра - орел и решка. \n" +
                "\n" +
                "Чтобы начать игру нужно выбрать сумму, которую вы хотите поставить. ");

        InlineKeyboardButton enterSum = new InlineKeyboardButton();
        enterSum.setText("Ввести сумму ставки");
        enterSum.setCallbackData("ENTER_SUM_RATE");

        List<InlineKeyboardButton> buttons = List.of(enterSum);
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
