package com.kingleaks.king_credits.bot;

import com.kingleaks.king_credits.bot.command.CommandRegistry;
import com.kingleaks.king_credits.config.BotConfig;
import com.kingleaks.king_credits.domain.entity.StatePaymentHistory;
import com.kingleaks.king_credits.service.StateManager;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
@Slf4j
public class KingCreditsBot extends TelegramLongPollingBot implements BotService {
    private final BotConfig botConfig;
    private final CommandRegistry commandRegistry;
    private final List<CallbackQueryHandler> callbackQueryHandlers;
    private final StateManager stateManager;

    @Autowired
    public KingCreditsBot(BotConfig botConfig, @Lazy CommandRegistry commandRegistry,
                          @Lazy List<CallbackQueryHandler> callbackQueryHandlers, StateManager stateManager) {
        this.botConfig = botConfig;
        this.commandRegistry = commandRegistry;
        this.callbackQueryHandlers = callbackQueryHandlers;
        this.stateManager = stateManager;
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
        checkStateManager(update);
        checkCommand(update);

        if (update.hasCallbackQuery()){
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String callbackData = callbackQuery.getData();

            for (CallbackQueryHandler handler : callbackQueryHandlers){
                if(handler.canHandle(callbackData)){
                    handler.handle(callbackQuery);
                    break;
                }
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

    @Override
    public void deleteMessage(DeleteMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void checkCommand(Update update){
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();

            switch (message) {
                case "/start":
                case "/home":
                    commandRegistry.getCommand("homecommand").execute(update);
                    break;
                case "Помощь":
                    commandRegistry.getCommand("helpstate").execute(update);
                    break;
                case "Профиль":
                    commandRegistry.getCommand("profilestate").execute(update);
                    break;
                case "Пополнить баланс":
                    commandRegistry.getCommand("topupbalancestate").execute(update);
                    break;
                default:
                    log.info("Unexpected message");
            }
        }
    }

    private void checkStateManager(Update update){
        if (update.hasMessage() && update.getMessage().hasText()){
            Long chatId = update.getMessage().getChatId();
            String messageText = update.getMessage().getText();
            Long telegramUserId = update.getMessage().getFrom().getId();
            StatePaymentHistory paymentHistory = stateManager.getUserState(telegramUserId);

            stateWaitingForAmount(paymentHistory, chatId, messageText, telegramUserId);
        }
    }

    private void stateWaitingForAmount(StatePaymentHistory paymentHistory,
                                       Long chatId, String messageText, Long telegramUserID){
        if (paymentHistory != null && "WAITING_FOR_AMOUNT".equals(paymentHistory.getStatus()) ){
                try {
                    double amount = Double.parseDouble(messageText);
                    System.out.println(amount);

                    SendMessage message = new SendMessage();
                    message.setChatId(chatId);
                    message.setText("Прекрасно! Теперь произведите оплату по одним из реквизитов ниже," +
                            "\nпосле отправьте чек сюда");
                    sendMessage(message);

                    paymentHistory.setStatus("COMPLETED");
                    stateManager.setUserState(telegramUserID, paymentHistory);
                    stateManager.deleteUserState(telegramUserID);
                } catch (NumberFormatException e) {
                    SendMessage message = new SendMessage();
                    message.setChatId(chatId);
                    message.setText("Ошибка: введите корректную сумму.");
                    sendMessage(message);
                }
            }

    }
}
