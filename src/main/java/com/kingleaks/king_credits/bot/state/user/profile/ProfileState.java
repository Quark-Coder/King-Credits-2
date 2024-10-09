package com.kingleaks.king_credits.bot.state.user.profile;

import com.kingleaks.king_credits.bot.BotService;
import com.kingleaks.king_credits.bot.command.Command;
import com.kingleaks.king_credits.repository.StateImageRepository;
import com.kingleaks.king_credits.service.TelegramUsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.ByteArrayInputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProfileState implements Command {
    private String stateName = "Профиль";
    private final StateImageRepository stateImageRepository;
    private final BotService botService;
    private final TelegramUsersService telegramUsersService;

    @Override
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        if (!stateImageRepository.isStateImageHasPictureByName(stateName)){
            byte[] photoData = stateImageRepository.findByNameState(stateName).getPhotoData();

            ByteArrayInputStream inputStream = new ByteArrayInputStream(photoData);
            InputFile inputFile = new InputFile(inputStream, "photo.jpg");
            SendPhoto returnPhoto = new SendPhoto();
            returnPhoto.setChatId(chatId.toString());
            returnPhoto.setPhoto(inputFile);
            returnPhoto.setCaption("\uD83D\uDD0E Информация о вас:\n");
            returnPhoto.setReplyMarkup(ReplyKeyboardMarkup.builder()
                    .keyboardRow(new KeyboardRow(List.of(new KeyboardButton("Назад")))).build());
            botService.sendPhoto(returnPhoto);
        } else {
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText("\uD83D\uDD0E Информация о вас:\n");
            message.setReplyMarkup(ReplyKeyboardMarkup.builder()
                    .keyboardRow(new KeyboardRow(List.of(new KeyboardButton("Меню")))).build());
            botService.sendMessage(message);
        }


        SendMessage userInfo = new SendMessage();
        userInfo.setChatId(update.getMessage().getChatId());

        String info = telegramUsersService.getInformationForProfile(
                update.getMessage().getFrom().getId());
        userInfo.setText(info);

        InlineKeyboardButton changeNick = new InlineKeyboardButton();
        changeNick.setText("✏\uFE0F Изменить ник\n");
        changeNick.setCallbackData("CHANGE_NICK");

        List<InlineKeyboardButton> buttons = List.of(changeNick);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(List.of(buttons));

        userInfo.setReplyMarkup(markup);
        botService.sendMessage(userInfo);
    }
}
