package com.kingleaks.king_credits.bot.state.profile;

import com.kingleaks.king_credits.bot.BotService;
import com.kingleaks.king_credits.bot.command.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class ProfileState implements Command {
    private final BotService botService;

    @Autowired
    public ProfileState(BotService botService) {
        this.botService = botService;
    }

    @Override
    public void execute(Update update) {
        SendMessage message = SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text("Это страница профиля. Здесь вы можете найти информацию о вашем профиле.")
                .build();

        botService.sendMessage(message);
    }
}
