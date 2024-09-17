package com.kingleaks.king_credits.bot.state.admin.create_promotional;

import com.kingleaks.king_credits.bot.BotService;
import com.kingleaks.king_credits.bot.command.Command;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CreatePromotionalState implements Command {
    private final BotService botService;

    @Override
    public void execute(Update update) {
        SendMessage message = SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text("Создать промокод")
                .build();
        message.setReplyMarkup(ReplyKeyboardMarkup.builder()
                .keyboardRow(new KeyboardRow(List.of(new KeyboardButton("Меню")))).build());
        botService.sendMessage(message);

        SendMessage result = new SendMessage();
        result.setChatId(update.getMessage().getChatId());
        result.setText("Промокод");

        InlineKeyboardButton createPromoCode = new InlineKeyboardButton();
        createPromoCode.setText("Создать промокод");
        createPromoCode.setCallbackData("CREATE_PROMOCODE");

        InlineKeyboardButton activePromoCode = new InlineKeyboardButton();
        activePromoCode.setText("Активные промокоды");
        activePromoCode.setCallbackData("ACTIVE_PROMOCODE");

        List<InlineKeyboardButton> buttons = new ArrayList<>();
        buttons.add(createPromoCode);
        buttons.add(activePromoCode);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(List.of(buttons));

        result.setReplyMarkup(markup);
        botService.sendMessage(result);
    }


}
