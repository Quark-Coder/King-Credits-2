package com.kingleaks.king_credits.bot.callback;

import com.kingleaks.king_credits.bot.BotService;
import com.kingleaks.king_credits.domain.entity.CasesItem;
import com.kingleaks.king_credits.service.AccountService;
import com.kingleaks.king_credits.service.CasesItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OpenCaseInventoryCallback implements CallbackQueryHandler{
    private final BotService botService;
    private final CasesItemService casesItemService;
    private final AccountService accountService;

    @Override
    public boolean canHandle(String callbackData) {
        String[] parts = callbackData.split("__");
        if (parts[0].equals("OPEN_CASE_INVENTORY")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void handle(CallbackQuery callbackQuery) {
        deleteMessage(callbackQuery);

        Long chatId = callbackQuery.getMessage().getChatId();
        String[] parts = callbackQuery.getData().split("__");
        Long telegramUserId = callbackQuery.getFrom().getId();
        Long inventoryId = Long.parseLong(parts[1]);

        CasesItem item = casesItemService.getRandomItem(inventoryId);
        accountService.replenish(telegramUserId, BigDecimal.valueOf(item.getPrice()));

        byte[] photoData = item.getPhotoData();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(photoData);
        InputFile inputFile = new InputFile(inputStream, "photo.jpg");
        SendPhoto returnPhoto = new SendPhoto();
        returnPhoto.setChatId(chatId.toString());
        returnPhoto.setPhoto(inputFile);
        returnPhoto.setCaption("\uD83E\uDD73 Поздравляем с выигрышем! \n\nТвой дроп: " +
                item.getName() + " cтоимость - " + item.getPrice() + "кредитов");
        returnPhoto.setReplyMarkup(ReplyKeyboardMarkup.builder()
                .keyboardRow(new KeyboardRow(List.of(new KeyboardButton("Назад")))).build());
        botService.sendPhoto(returnPhoto);
    }

    private void deleteMessage(CallbackQuery callbackQuery) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(callbackQuery.getMessage().getChatId());
        deleteMessage.setMessageId(callbackQuery.getMessage().getMessageId());
        botService.deleteMessage(deleteMessage);
    }
}
