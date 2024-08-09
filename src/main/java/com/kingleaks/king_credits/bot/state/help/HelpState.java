package com.kingleaks.king_credits.bot.state.help;

import com.kingleaks.king_credits.bot.command.Command;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Slf4j
public class HelpState implements Command {
    private final TelegramLongPollingBot bot;

    @Autowired
    public HelpState(TelegramLongPollingBot bot) {
        this.bot = bot;
    }

    @Override
    public void execute(long chatId) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text("Это страница помощи. Здесь вы можете найти ответы на часто задаваемые вопросы.")
                .build();

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}
