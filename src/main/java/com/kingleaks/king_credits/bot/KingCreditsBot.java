package com.kingleaks.king_credits.bot;

import com.kingleaks.king_credits.bot.callback.CallbackQueryHandler;
import com.kingleaks.king_credits.bot.command.CommandRegistry;
import com.kingleaks.king_credits.bot.waitingState.*;
import com.kingleaks.king_credits.config.BotConfig;
import com.kingleaks.king_credits.domain.entity.PaymentCheckPhoto;
import com.kingleaks.king_credits.domain.entity.StatePaymentHistory;
import com.kingleaks.king_credits.domain.enums.UserStatus;
import com.kingleaks.king_credits.service.*;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Component
@Slf4j
public class KingCreditsBot extends TelegramLongPollingBot implements BotService {
    private final BotConfig botConfig;
    private final CommandRegistry commandRegistry;
    private final List<CallbackQueryHandler> callbackQueryHandlers;
    private final StateManagerService stateManager;
    private final SubscriptionVerificationService subscriptionVerificationService;
    private final PaymentCheckPhotoService paymentCheckPhotoService;
    private final TelegramUsersService telegramUsersService;
    private final StateWaitingForAmount stateWaitingForAmount;
    private final StateWaitingForChangeNickname stateWaitingForChangeNickname;
    private final StateWaitingForCreditsInRub stateWaitingForCreditsInRub;
    private final StateWaitingForRubInCredits stateWaitingForRubInCredits;
    private final StateWaitingForWithdrawalOfCredits stateWaitingForWithdrawalOfCredits;
    private final WithdrawalOfCreditsService withdrawalOfCreditsService;
    private final StateWaitingForWithdrawalNick stateWaitingForWithdrawalNick;
    private final StateWaitingForSelectRequest stateWaitingForSelectRequest;

    @Autowired
    public KingCreditsBot(BotConfig botConfig, @Lazy CommandRegistry commandRegistry,
                          @Lazy List<CallbackQueryHandler> callbackQueryHandlers,
                          StateManagerService stateManager, @Lazy SubscriptionVerificationService subscriptionVerificationService,
                          PaymentCheckPhotoService paymentCheckPhotoService, TelegramUsersService telegramUsersService,
                          StateWaitingForAmount stateWaitingForAmount, StateWaitingForChangeNickname stateWaitingForChangeNickname,
                          StateWaitingForCreditsInRub stateWaitingForCreditsInRub, StateWaitingForRubInCredits stateWaitingForRubInCredits,
                          StateWaitingForWithdrawalOfCredits stateWaitingForWithdrawalOfCredits,
                          WithdrawalOfCreditsService withdrawalOfCreditsService, StateWaitingForWithdrawalNick stateWaitingForWithdrawalNick,
                          @Lazy StateWaitingForSelectRequest stateWaitingForSelectRequest) {
        this.botConfig = botConfig;
        this.commandRegistry = commandRegistry;
        this.callbackQueryHandlers = callbackQueryHandlers;
        this.stateManager = stateManager;
        this.subscriptionVerificationService = subscriptionVerificationService;
        this.paymentCheckPhotoService = paymentCheckPhotoService;
        this.telegramUsersService = telegramUsersService;
        this.stateWaitingForAmount = stateWaitingForAmount;
        this.stateWaitingForChangeNickname = stateWaitingForChangeNickname;
        this.stateWaitingForCreditsInRub = stateWaitingForCreditsInRub;
        this.stateWaitingForRubInCredits = stateWaitingForRubInCredits;
        this.stateWaitingForWithdrawalOfCredits = stateWaitingForWithdrawalOfCredits;
        this.withdrawalOfCreditsService = withdrawalOfCreditsService;
        this.stateWaitingForWithdrawalNick = stateWaitingForWithdrawalNick;
        this.stateWaitingForSelectRequest = stateWaitingForSelectRequest;
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
        if (update.hasMessage() && update.getMessage().hasText()){
            if (update.getMessage().getText().equals("/start")) {
                Long telegramUserId = update.getMessage().getFrom().getId();
                Long chatId = update.getMessage().getChatId();
                String firstName = update.getMessage().getFrom().getFirstName();
                String lastName = update.getMessage().getFrom().getLastName();
                String nickname = update.getMessage().getFrom().getUserName();

                telegramUsersService.registerUser(telegramUserId, chatId , firstName, lastName, nickname);
            }
        }
        checkStateManager(update);
        if (update.hasMessage() && update.getMessage().hasText()){
            UserStatus userStatus = telegramUsersService.getStatus(update.getMessage().getFrom().getId());
            if (userStatus.name().equals("ADMIN")){
                checkCommandForAdmin(update);
            } else {
                checkCommand(update);
            }
        }
        checkCallback(update);
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

    @Override
    public void sendPhoto(SendPhoto message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    public void checkCommandForAdmin(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();

            switch (message) {
                case "/start":
                case "/home":
                case "Назад":
                case "Меню":
                    commandRegistry.getCommand("homecommand").execute(update);
                    break;
                case "Запросы на пополнение":
                    commandRegistry.getCommand("replenishmentrequestsstate").execute(update);
                    break;
                case "Запросы на вывод":
                    commandRegistry.getCommand("withdrawalrequestsstate").execute(update);
                    break;
                case "Все пользователи":
                    commandRegistry.getCommand("allusersstate").execute(update);
                    break;
                case "Создать промокод":
                    commandRegistry.getCommand("createpromotionalstate").execute(update);
                    break;
                case "Черный список":
                    commandRegistry.getCommand("blackliststate").execute(update);
                    break;
                case "Статистика":
                    commandRegistry.getCommand("statisticsstate").execute(update);
                    break;
                default:
                    log.info("Unexpected message");
                }

        }
    }

    private void checkCommand(Update update){
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long telegramUserId = update.getMessage().getFrom().getId();
            if (!subscriptionVerificationService.verifySubscription(telegramUserId)) {
                SendMessage message = new SendMessage();
                message.setChatId(update.getMessage().getChatId().toString());
                message.setText("Пожалуста подпишитесь на этот канал прежде чем пользоваться ботом" +
                        "\n<a href=\"https://t.me/xrayduru/15\">Подписаться на канал</a>");
                message.setParseMode("HTML");
                message.setReplyMarkup(ReplyKeyboardMarkup.builder()
                        .keyboardRow(new KeyboardRow(List.of(new KeyboardButton("Подписался")))).build());

                sendMessage(message);
            } else {
                String message = update.getMessage().getText();

                switch (message) {
                    case "/start":
                    case "/home":
                    case "Назад":
                    case "Меню":
                        StatePaymentHistory paymentHistory = stateManager.getUserState(telegramUserId);
                        SendMessage errorMessage = new SendMessage();
                        errorMessage.setChatId(update.getMessage().getChatId());
                        if (paymentHistory != null && "WAITING_FOR_AMOUNT".equals(paymentHistory.getStatus()) ){
                            errorMessage.setText("Вы не указали сумму пополнения счета, напишите:");
                            sendMessage(errorMessage);
                        } else if (paymentHistory != null && "WAITING_FOR_PAYMENT_CHECK".equals(paymentHistory.getStatus())){
                            errorMessage.setText("Вы не отправили чек оплаты вашего заказа, отправьте:");
                            sendMessage(errorMessage);
                        } else {
                            commandRegistry.getCommand("homecommand").execute(update);
                        }
                        break;
                    case "Подписался":
                        commandRegistry.getCommand("homecommand").execute(update);
                        break;
                    case "Пополнить баланс":
                        commandRegistry.getCommand("topupbalancestate").execute(update);
                        break;
                    case "Профиль":
                        commandRegistry.getCommand("profilestate").execute(update);
                        break;
                    case "Вывод кредитов":
                        commandRegistry.getCommand("withdrawalofcreditsstate").execute(update);
                        break;
                    case "Актуальный курс":
                        commandRegistry.getCommand("currentratestate").execute(update);
                        break;
                    case "Посчитать":
                        commandRegistry.getCommand("calculatestate").execute(update);
                        break;
                    case "Помощь":
                        commandRegistry.getCommand("helpstate").execute(update);
                        break;
                    case "Продать кредиты":
                        commandRegistry.getCommand("sellcreditsstate").execute(update);
                        break;
                    case "Отзывы":
                        commandRegistry.getCommand("reviewsstate").execute(update);
                        break;
                    default:
                        log.info("Unexpected message");
                }
            }
        }
    }

    private void checkStateManager(Update update){
        if (update.hasMessage()){
            Long chatId = update.getMessage().getChatId();
            String messageText = update.getMessage().getText();
            Long telegramUserId = update.getMessage().getFrom().getId();
            StatePaymentHistory paymentHistory = stateManager.getUserState(telegramUserId);

            if (update.getMessage().hasText() && paymentHistory != null){
                switch (paymentHistory.getStatus()){
                    case "WAITING_FOR_AMOUNT":
                        sendMessage(stateWaitingForAmount
                                .waitingForAmount(paymentHistory, chatId, messageText, telegramUserId));
                        break;
                    case "WAITING_FOR_CHANGE_NICKNAME":
                        sendMessage(stateWaitingForChangeNickname
                                .waitingForChangeNickname(paymentHistory, chatId, messageText, telegramUserId));
                        break;
                    case "WAITING_FOR_CALCULATION_CREDITSRUB":
                        sendMessage(stateWaitingForCreditsInRub
                                .waitingForCreditsInRub(paymentHistory, chatId, messageText, telegramUserId));
                        break;
                    case "WAITING_FOR_CALCULATION_RUBCREDITS":
                        sendMessage(stateWaitingForRubInCredits
                                .waitingForRubInCredits(paymentHistory, chatId, messageText, telegramUserId));
                        break;
                    case "WAITING_FOR_AMOUNT_FOR_WITHDRAWAL":
                        sendMessage(stateWaitingForWithdrawalOfCredits
                                .waitingForWithdrawalOfCredits(paymentHistory, chatId, messageText, telegramUserId));
                        break;
                    case "WAITING_FOR_WITHDRAWAL_NICK":
                        sendMessage(stateWaitingForWithdrawalNick
                                .waitingForWithdrawalNick(paymentHistory, chatId, messageText, telegramUserId));
                        break;
                    case "WAITING_FOR_SELECT_REQUEST":
                        sendMessage(stateWaitingForSelectRequest
                                .waitingForSelectRequest(paymentHistory, chatId, messageText, telegramUserId));
                        break;
                }
            } else if (update.getMessage().hasPhoto()){
                List<PhotoSize> photos = update.getMessage().getPhoto();
                PhotoSize photo = photos.get(photos.size() - 1);

                stateWaitingForPhoto(paymentHistory, chatId, photo, telegramUserId);
            }
        }
    }

    private void checkCallback(Update update){
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

    private void stateWaitingForPhoto(StatePaymentHistory paymentHistory,
                                      Long chatId, PhotoSize photo, Long telegramUserId){
        if(paymentHistory != null && "WAITING_FOR_PAYMENT_CHECK".equals(paymentHistory.getStatus())){

            String fileId = photo.getFileId();
            PaymentCheckPhoto paymentCheckPhoto = savePhotoCheckToDatabase(fileId, telegramUserId);
            byte[] photoData = paymentCheckPhoto.getPhotoData();

            ByteArrayInputStream inputStream = new ByteArrayInputStream(photoData);
            InputFile inputFile = new InputFile(inputStream, "photo.jpg");

            stateManager.deleteUserState(telegramUserId);
            // Создаем объект SendPhoto
            SendPhoto returnPhoto = new SendPhoto();
            returnPhoto.setChatId(chatId.toString());
            returnPhoto.setPhoto(inputFile);

            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText("Хорошо, когда мы проверим оплату," +
                    " деньги будут засчитаны на ваш баланс! Номер вашего чека: " + paymentCheckPhoto.getId());

            sendPhoto(returnPhoto);
            sendMessage(message);
        } else if (paymentHistory != null && "WAITING_FOR_AMOUNT_WITHDRAWAL_PHOTO".equals(paymentHistory.getStatus())) {
            String fileId = photo.getFileId();
            savePhotoWithdrawalToDatabase(fileId, telegramUserId);

            paymentHistory.setStatus("WAITING_FOR_WITHDRAWAL_NICK");
            stateManager.setUserState(telegramUserId, paymentHistory);

            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText("Хорошо, теперь отправьте нам свой никнейм из игры");

            sendMessage(message);
        }
    }

    public PaymentCheckPhoto savePhotoCheckToDatabase(String fileId, Long telegramUserId) {
        byte[] photoData = downloadPhoto(fileId);
        if (photoData != null) {
            return paymentCheckPhotoService.savePhoto(photoData, telegramUserId);
        } else {
            return null;
        }
    }

    public void savePhotoWithdrawalToDatabase(String fileId, Long telegramUserId) {
        byte[] photoData = downloadPhoto(fileId);
        if (photoData != null) {
            withdrawalOfCreditsService.initPhotoWithdrawalOfCredits(telegramUserId, photoData);
        }
    }

    public byte[] downloadPhoto(String fileId) {
        GetFile getFileMethod = new GetFile();
        getFileMethod.setFileId(fileId);
        try {
            File file = execute(getFileMethod);
            String filePath = file.getFilePath();
            java.io.File downloadedFile = downloadFile(filePath);
            return Files.readAllBytes(downloadedFile.toPath());
        } catch (TelegramApiException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
