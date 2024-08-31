package com.kingleaks.king_credits.bot.waitingState;

import com.kingleaks.king_credits.bot.BotService;
import com.kingleaks.king_credits.domain.entity.PaymentCheckPhoto;
import com.kingleaks.king_credits.domain.entity.StatePaymentHistory;
import com.kingleaks.king_credits.domain.entity.TelegramUsers;
import com.kingleaks.king_credits.service.ReplenishmentRequestsService;
import com.kingleaks.king_credits.service.StateManagerService;
import com.kingleaks.king_credits.service.TelegramUsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StateWaitingForSelectRequest implements StateWaitingQueryHandler {
    private final BotService botService;
    private final ReplenishmentRequestsService replenishmentRequestsService;
    private final TelegramUsersService telegramUsersService;
    private final StateManagerService stateManager;

    public SendMessage waitingForSelectRequest(StatePaymentHistory paymentHistory,
                                        Long chatId, String messageText, Long telegramUserID){
        if (paymentHistory != null){
            try {
                Long selectId = Long.parseLong(messageText);
                PaymentCheckPhoto paymentCheckPhoto = replenishmentRequestsService.selectPaymentCheckPhotoById(selectId);
                if (paymentCheckPhoto != null){
                    Double amount = paymentCheckPhoto.getPrice();
                    LocalDateTime dateTime = paymentCheckPhoto.getCreatedAt();
                    TelegramUsers users = telegramUsersService.findById(paymentCheckPhoto.getTelegramUserId());
                    String firstName = users.getFirstName();
                    String lastName = users.getLastName();
                    String nickname = users.getNickname() == null ? "Нету ника" : "<a href=\"https://t.me/" + users.getNickname() + "\">" + users.getNickname() + "</a>";

                    byte[] photoData = paymentCheckPhoto.getPhotoData();

                    ByteArrayInputStream inputStream = new ByteArrayInputStream(photoData);
                    InputFile inputFile = new InputFile(inputStream, "photo.jpg");
                    SendPhoto returnPhoto = new SendPhoto();
                    returnPhoto.setChatId(chatId.toString());
                    returnPhoto.setPhoto(inputFile);
                    botService.sendPhoto(returnPhoto);

                    SendMessage message = new SendMessage();
                    message.setChatId(chatId);

                    String result = firstName + " " + lastName +
                            "\nНомер чека - " + String.format("%05d", selectId) +
                            "\nНик из телеграмм - " + nickname +
                            "\nДата заявки - " + dateTime +
                            "\nСумма - " + amount;

                    InlineKeyboardButton confirm = new InlineKeyboardButton();
                    confirm.setText("Подтвердить");
                    confirm.setCallbackData("CONFIRMREQUEST_" + selectId);
                    InlineKeyboardButton reject = new InlineKeyboardButton();
                    reject.setText("Отклонить");
                    reject.setCallbackData("REJECTREQUEST_" + selectId);
                    InlineKeyboardButton error = new InlineKeyboardButton();
                    error.setText("Ошибка");
                    error.setCallbackData("ERRORREQUEST_" + selectId);

                    List<InlineKeyboardButton> buttons = List.of(confirm, reject, error);
                    InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
                    markup.setKeyboard(List.of(buttons));

                    message.setParseMode("HTML");
                    message.setText(result);
                    message.setReplyMarkup(markup);

                    stateManager.deleteUserState(telegramUserID);
                    return message;
                } else {
                    SendMessage message = new SendMessage();
                    message.setChatId(chatId);
                    message.setText("Вы не указали номер чека из листа, пожалуйста укажите правильно");
                    return message;
                }
            } catch (NumberFormatException e) {
                SendMessage message = new SendMessage();
                message.setChatId(chatId);
                message.setText("Ошибка: введите корректное число.");

                return message;
            }
        }
        return null;
    }

    @Override
    public boolean canHandle(String stateStatus) {
        return "WAITING_FOR_SELECT_REQUEST".equals(stateStatus);
    }

    @Override
    public SendMessage handle(StatePaymentHistory paymentHistory, Long chatId, String messageText, Long telegramUserID) {
        return waitingForSelectRequest(paymentHistory, chatId, messageText, telegramUserID);
    }
}
