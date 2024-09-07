package com.kingleaks.king_credits.bot.state.user.buy_skins;

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
public class BuySkinsState implements Command {
    private final BotService botService;

    @Override
    public void execute(Update update) {
        SendMessage message = SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text("Купить скины")
                .build();
        botService.sendMessage(message);

        SendMessage option = new SendMessage();
        option.setChatId(update.getMessage().getChatId());

        InlineKeyboardButton urlStore = new InlineKeyboardButton();
        urlStore.setText("Cсылка на магазин скинов");
        urlStore.setUrl("https://youtu.be/dQw4w9WgXcQ?si=5geLgShGreJXCU-w");

        InlineKeyboardButton urlProfile = new InlineKeyboardButton();
        urlProfile.setText("Cсылка на наш профиль");
        urlProfile.setUrl("https://youtu.be/L_jWHffIx5E?si=wzPJhQFExlYSroct");

        List<InlineKeyboardButton> buttons = List.of(urlStore, urlProfile);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(List.of(buttons));

        option.setText("Выберите опцию");
        option.setReplyMarkup(markup);
        botService.sendMessage(option);
    }
}
