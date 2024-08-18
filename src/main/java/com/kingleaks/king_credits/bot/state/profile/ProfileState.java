package com.kingleaks.king_credits.bot.state.profile;

import com.kingleaks.king_credits.bot.BotService;
import com.kingleaks.king_credits.bot.command.Command;
import com.kingleaks.king_credits.service.TelegramUsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProfileState implements Command {
    private final BotService botService;
    private final TelegramUsersService telegramUsersService;

    @Override
    public void execute(Update update) {
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId().toString());
        message.setText("Информация о вас");
        message.setReplyMarkup(ReplyKeyboardMarkup.builder()
                .keyboardRow(new KeyboardRow(List.of(new KeyboardButton("Меню")))).build());
        botService.sendMessage(message);

        SendMessage userInfo = new SendMessage();
        userInfo.setChatId(update.getMessage().getChatId());

        String info = telegramUsersService.getInformationForProfile(
                update.getMessage().getFrom().getId());
        userInfo.setText(info);

        InlineKeyboardButton changeNick = new InlineKeyboardButton();
        changeNick.setText("Изменить ник");
        changeNick.setCallbackData("CHANGE_NICK");

        List<InlineKeyboardButton> buttons = List.of(changeNick);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(List.of(buttons));

        userInfo.setReplyMarkup(markup);
        botService.sendMessage(userInfo);
    }
}
