package com.kingleaks.king_credits.bot;

import com.kingleaks.king_credits.bot.command.CommandRegistry;
import com.kingleaks.king_credits.config.BotConfig;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Slf4j
public class KingCreditsBot extends TelegramLongPollingBot implements BotService {
    private final BotConfig botConfig;
    private final CommandRegistry commandRegistry;

    @Autowired
    public KingCreditsBot(BotConfig botConfig, @Lazy CommandRegistry commandRegistry) {
        this.botConfig = botConfig;
        this.commandRegistry = commandRegistry;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken(){
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(@NotNull Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (message) {
                case "/start":
                case "/home":
                    commandRegistry.getCommand("homecommand").execute(chatId);
                    break;
                case "Помощь":
                    commandRegistry.getCommand("helpstate").execute(chatId);
                default:
                    log.info("Unexpected message");
            }
        }
    }

    @Override
    public void sendMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}
