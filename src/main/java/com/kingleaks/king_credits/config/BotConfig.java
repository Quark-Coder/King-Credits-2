package com.kingleaks.king_credits.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
@Getter
@Setter
@PropertySource(value = "classpath:application.yaml")
public class BotConfig {
    @Value("${bot.name}") String botName;
    @Value("${bot.token}") String token;
}
