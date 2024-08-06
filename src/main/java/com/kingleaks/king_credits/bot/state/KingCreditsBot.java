package com.kingleaks.king_credits.bot.state;

import com.kingleaks.king_credits.bot.BotCommands;
import com.kingleaks.king_credits.config.BotConfig;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
@Slf4j
public class KingCreditsBot extends TelegramLongPollingBot implements BotCommands {
    private final BotConfig botConfig;

    @Autowired
    public KingCreditsBot(BotConfig config) {
        this.botConfig = config;
        try {
            this.execute(new SetMyCommands(LIST_OF_COMMANDS, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e){
            log.error(e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

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
                    home(chatId);
                    break;
                default:
                    log.info("Unexpected message");
            }
        }
    }

    private void home(long chatId) {
        SendMessage message = SendMessage
                .builder()
                .chatId(chatId)
                .text("Это главная страница")
                .build();

        List<KeyboardButton> btn1 = List.of(
                new KeyboardButton("Пополнить баланс"),
                new KeyboardButton("Профиль")
        );
        List<KeyboardButton> btn2 = List.of(
                new KeyboardButton("Вывод кредитов"),
                new KeyboardButton("Актуальный курс")
        );
        List<KeyboardButton> btn3 = List.of(
                new KeyboardButton("Посчитать"),
                new KeyboardButton("Помощь")
        );
        List<KeyboardButton> btn4 = List.of(
                new KeyboardButton("Продать кредиты"),
                new KeyboardButton("Отзывы")
        );

        message.setReplyMarkup(ReplyKeyboardMarkup
                .builder()
                .keyboardRow(new KeyboardRow(btn1))
                .keyboardRow(new KeyboardRow(btn2))
                .keyboardRow(new KeyboardRow(btn3))
                .keyboardRow(new KeyboardRow(btn4))
                .build());
        try {
            execute(message);
            log.info("Reply sent");
        } catch (TelegramApiException e){
            log.error(e.getMessage());
        }
    }
}
