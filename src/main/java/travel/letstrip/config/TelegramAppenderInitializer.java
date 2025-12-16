package travel.letstrip.config;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.Logger;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import travel.letstrip.config.TelegramClient;
import travel.letstrip.telegram.TelegramLogbackAppender;
import travel.letstrip.telegram.TelegramProperties;

@Component
@RequiredArgsConstructor
public class TelegramAppenderInitializer {

    private final TelegramClient telegramClient;
    private final TelegramProperties properties;

    @PostConstruct
    public void init() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        TelegramLogbackAppender appender = new TelegramLogbackAppender();

        appender.setContext(context);
        appender.setTelegramClient(telegramClient);
        appender.setProperties(properties);
        appender.start();

        Logger rootLogger = context.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.addAppender(appender);
    }
    @Bean
    public TelegramClient telegramClient() {
        return new TelegramClient(properties);
    }
}
