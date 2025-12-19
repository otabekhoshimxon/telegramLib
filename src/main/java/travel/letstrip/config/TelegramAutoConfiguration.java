package travel.letstrip.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import travel.letstrip.telegram.TelegramProperties;

@Configuration
@EnableConfigurationProperties(TelegramProperties.class)
public class TelegramAutoConfiguration {
    public TelegramAutoConfiguration() {
    }
}
