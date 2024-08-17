package com.kingleaks.king_credits.bot.state.calculate;

import com.kingleaks.king_credits.bot.command.Command;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class CalculateState implements Command {
    @Override
    public void execute(Update update) {

    }
}
