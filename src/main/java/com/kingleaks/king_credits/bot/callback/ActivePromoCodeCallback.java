package com.kingleaks.king_credits.bot.callback;

import com.kingleaks.king_credits.bot.BotService;
import com.kingleaks.king_credits.service.PromoCodeService;
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
public class ActivePromoCodeCallback implements CallbackQueryHandler {
    private final BotService botService;
    private final PromoCodeService promoCodeService;

    @Override
    public boolean canHandle(String callbackData) {
        return "ACTIVE_PROMOCODE".equals(callbackData);
    }

    @Override
    public void handle(CallbackQuery callbackQuery) {
        deleteMessage(callbackQuery);
        SendMessage option = new SendMessage();
        option.setChatId(callbackQuery.getMessage().getChatId());
        String result = promoCodeService.getAllActivePromoCodes();
        option.setText(result);

        InlineKeyboardButton selectCase = new InlineKeyboardButton();
        selectCase.setText("Выбрать промокод");
        selectCase.setCallbackData("SELECT_PROMOCODE");

        List<InlineKeyboardButton> buttons = List.of(selectCase);
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
