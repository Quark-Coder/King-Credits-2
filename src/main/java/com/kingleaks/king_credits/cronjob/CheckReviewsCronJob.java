package com.kingleaks.king_credits.cronjob;

import com.kingleaks.king_credits.bot.BotService;
import com.kingleaks.king_credits.domain.entity.Reviews;
import com.kingleaks.king_credits.domain.entity.TelegramUsers;
import com.kingleaks.king_credits.domain.enums.ReviewsStatus;
import com.kingleaks.king_credits.repository.ReviewsRepository;
import com.kingleaks.king_credits.service.TelegramUsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.time.LocalDate;
import java.util.List;

@EnableScheduling
@Service
@Slf4j
@RequiredArgsConstructor
public class CheckReviewsCronJob {
    private static final String GMT_3 = "GMT+3";
    private final TelegramUsersService telegramUsersService;
    private final ReviewsRepository reviewsRepository;
    private final BotService botService;

    @Scheduled(cron = "0 0 * * * *", zone = GMT_3)
    public void checkReviews() {
        List<Reviews> reviewsWithComment = reviewsRepository.findAllReviewsWithComment();
        List<Reviews> reviewsWithoutComment = reviewsRepository.findAllReviewsWithoutComment();
        List<TelegramUsers> admins = telegramUsersService.findAllAdmins();

        for (TelegramUsers admin : admins){
            sendMessageForAdmin(reviewsWithComment, admin);
            sendMessageForAdmin(reviewsWithoutComment, admin);
        }

        log.info("Проверили список отзывов");
    }

    private void sendMessageForAdmin(List<Reviews> reviews, TelegramUsers admin) {
        for (Reviews review : reviews){
            TelegramUsers telegramUsers = telegramUsersService.findById(review.getTelegramUserId());

            String nickname = telegramUsers.getNickname() == null ? "Нету ника" : "<a href=\"https://t.me/"
                    + telegramUsers.getNickname() + "\">" + telegramUsers.getNickname() + "</a>";
            String comment = review.getComment() == null ? "Не комментировал" : review.getComment();

            String reviewMessage = "\uD83D\uDC64 Заказ №" + String.format("%05d", review.getPaymentCheckPhotoId()) + ": "
                    + telegramUsers.getFirstName() +
                    "\n\nКомментарий: " + comment +
                    "\n\n" + LocalDate.now() + " Купил: " + nickname;

            SendMessage sendMessage = SendMessage.builder()
                    .chatId(admin.getChatId())
                    .text(reviewMessage)
                    .build();

            review.setStatus(ReviewsStatus.COMPLETED);
            reviewsRepository.save(review);

            botService.sendMessage(sendMessage);
        }
        log.info("Отправили загатовленный отзыв админам");
    }
}
