package com.kingleaks.king_credits.bot.state.user.leaderboard;

import com.kingleaks.king_credits.bot.BotService;
import com.kingleaks.king_credits.bot.command.Command;
import com.kingleaks.king_credits.service.TelegramUsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LeaderboardState implements Command {
    private final TelegramUsersService telegramUsersService;
    private final BotService botService;

    @Override
    public void execute(Update update) {
        String result = telegramUsersService.getLeaderboard();
        SendMessage message = SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text("Вот список лидеров:\n" + result)
                .build();
        message.setReplyMarkup(ReplyKeyboardMarkup.builder()
                .keyboardRow(new KeyboardRow(List.of(new KeyboardButton("Назад")))).build());
        botService.sendMessage(message);
    }
}
