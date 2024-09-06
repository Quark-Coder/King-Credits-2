package com.kingleaks.king_credits.bot.state.admin.add_photo_skins_for_sale;

import com.kingleaks.king_credits.bot.BotService;
import com.kingleaks.king_credits.bot.command.Command;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AddPhotoSkinsForSaleState implements Command {
    private final BotService botService;

    @Override
    public void execute(Update update) {
        SendMessage message = SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text("Загрузить картинки скинов для продаж")
                .build();

        InlineKeyboardButton selectItem = new InlineKeyboardButton();
        selectItem.setText("Загрузить изображение");
        selectItem.setCallbackData("UPLOAD_PHOTO_SKINS_FOR_SALE");

        List<InlineKeyboardButton> buttons = List.of(selectItem);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(List.of(buttons));

        message.setReplyMarkup(markup);
        botService.sendMessage(message);
    }
}
