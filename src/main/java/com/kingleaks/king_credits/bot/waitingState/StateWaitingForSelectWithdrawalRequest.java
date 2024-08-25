package com.kingleaks.king_credits.bot.waitingState;

import com.kingleaks.king_credits.bot.BotService;
import com.kingleaks.king_credits.domain.entity.PaymentCheckPhoto;
import com.kingleaks.king_credits.domain.entity.StatePaymentHistory;
import com.kingleaks.king_credits.domain.entity.TelegramUsers;
import com.kingleaks.king_credits.domain.entity.WithdrawalOfCredits;
import com.kingleaks.king_credits.service.ReplenishmentRequestsService;
import com.kingleaks.king_credits.service.StateManagerService;
import com.kingleaks.king_credits.service.TelegramUsersService;
import com.kingleaks.king_credits.service.WithdrawalOfCreditsService;
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
public class StateWaitingForSelectWithdrawalRequest {
    private final BotService botService;
    private final WithdrawalOfCreditsService withdrawalOfCreditsService;
    private final TelegramUsersService telegramUsersService;
    private final StateManagerService stateManager;

    public SendMessage waitingForSelectWithdrawalRequest(StatePaymentHistory paymentHistory,
                                                         Long chatId, String messageText, Long telegramUserID) {
        if (paymentHistory != null){
            try {
                Long selectId = Long.parseLong(messageText);
                WithdrawalOfCredits withdrawalOfCredits = withdrawalOfCreditsService.selectWithdrawalOfCreditsById(selectId);
                if (withdrawalOfCredits != null){
                    Double amount = withdrawalOfCredits.getPrice();
                    LocalDateTime dateTime = withdrawalOfCredits.getCreatedAt();
                    TelegramUsers users = telegramUsersService.findById(withdrawalOfCredits.getTelegramUserId());
                    String firstName = users.getFirstName();
                    String lastName = users.getLastName();

                    byte[] photoData = withdrawalOfCredits.getPhoto();

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
                            "\nДата заявки - " + dateTime +
                            "\nСумма - " + amount +
                            "\nНик из игры - " + withdrawalOfCredits.getNickInGame();

                    InlineKeyboardButton confirm = new InlineKeyboardButton();
                    confirm.setText("Принять");
                    confirm.setCallbackData("CONFIRMWITHDRAWAL_" + selectId);
                    InlineKeyboardButton reject = new InlineKeyboardButton();
                    reject.setText("Отклонить");
                    reject.setCallbackData("REJECTWITHDRAWAL_" + selectId);

                    List<InlineKeyboardButton> buttons = List.of(confirm, reject);
                    InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
                    markup.setKeyboard(List.of(buttons));

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
}
