package com.kingleaks.king_credits.bot.callback;

import com.kingleaks.king_credits.bot.BotService;
import com.kingleaks.king_credits.domain.entity.TelegramUsers;
import com.kingleaks.king_credits.service.ReviewsService;
import com.kingleaks.king_credits.service.WithdrawalOfCreditsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ConfirmWithdrawalCallback implements CallbackQueryHandler {
    private final BotService botService;
    private final WithdrawalOfCreditsService withdrawalOfCreditsService;
    private final ReviewsService reviewsService;

    @Override
    public boolean canHandle(String callbackData) {
        String[] parts = callbackData.split("_");
        if (parts[0].equals("CONFIRMWITHDRAWAL")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void handle(CallbackQuery callbackQuery) {
        deleteMessage(callbackQuery);
        Long chatId = callbackQuery.getMessage().getChatId();
        String[] parts = callbackQuery.getData().split("_");
        Long id = Long.parseLong(parts[1]);
        TelegramUsers telegramUsers = withdrawalOfCreditsService.confirmRequest(id);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Вы подтвердили заказ");
        sendMessage.setParseMode("HTML");
        botService.sendMessage(sendMessage);

        reviewsService.createReview(telegramUsers.getUserId(), id);

        InlineKeyboardButton reviews = new InlineKeyboardButton();
        reviews.setText("Оставить отзыв");
        reviews.setCallbackData("REVIEWSCALLBACK_" + id);

        List<InlineKeyboardButton> buttons = List.of(reviews);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(List.of(buttons));

        SendMessage sendMessageForUser = new SendMessage();
        sendMessageForUser.setChatId(telegramUsers.getChatId());
        sendMessageForUser.setText("Вашу заявку подтвердили, можете оставить отзыв ?)");
        sendMessageForUser.setReplyMarkup(markup);
        botService.sendMessage(sendMessageForUser);
    }

    private void deleteMessage(CallbackQuery callbackQuery) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(callbackQuery.getMessage().getChatId());
        deleteMessage.setMessageId(callbackQuery.getMessage().getMessageId());
        botService.deleteMessage(deleteMessage);
    }
}
