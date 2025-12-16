package travel.letstrip.telegram;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "telegram")
public class TelegramProperties {

    /**
     * Bot sozlamalari
     */
    private Bot bot = new Bot();

    /**
     * Group sozlamalari
     */
    private Group group = new Group();

    /**
     * Logging sozlamalari
     */
    private Logging logging = new Logging();

    /**
     * Message sozlamalari
     */
    private Message message = new Message();

    @Data
    public static class Bot {
        /**
         * Bot'ni yoqish/o'chirish
         */
        private boolean enabled = false;

        /**
         * Bot token
         */
        private String token;



        /**
         * Webhook mode (false = long polling)
         */
        private boolean webhook = false;

        /**
         * Webhook URL
         */
        private String webhookUrl;
    }

    @Data
    public static class Group {
        /**
         * Asosiy group/chat ID
         */
        private String id;

        /**
         * Topic/Thread ID (ixtiyoriy)
         */
        private String topicId;

        /**
         * Admin group ID (alohida)
         */
        private String adminId;

        /**
         * Error notification group ID
         */
        private String errorGroupId;
    }

    @Data
    public static class Logging {
        /**
         * Telegram logging'ni yoqish
         */
        private boolean enabled = false;

        /**
         * Qaysi level'larni yuborish (ERROR, WARN, INFO)
         */
        private String level = "ERROR,WARN";

        /**
         * Stack trace'ni qo'shish
         */
        private boolean includeStackTrace = true;

        /**
         * Maximum stack trace lines
         */
        private int maxStackTraceLines = 5;

        /**
         * Async yuborish
         */
        private boolean async = true;

        /**
         * Queue size
         */
        private int queueSize = 1000;
    }

    @Data
    public static class Message {
        /**
         * Maximum message uzunligi
         */
        private int maxLength = 4000;

        /**
         * Parse mode (HTML, Markdown, MarkdownV2)
         */
        private String parseMode = "HTML";

        /**
         * Web page preview'ni o'chirish
         */
        private boolean disableWebPagePreview = true;

        /**
         * Notification'ni o'chirish
         */
        private boolean disableNotification = false;

        /**
         * Rate limiting (messages per minute)
         */
        private int rateLimit = 30;
    }

    // Helper methods

    public boolean isBotEnabled() {
        return bot.enabled && hasValidBotConfig();
    }

    public boolean isLoggingEnabled() {
        return logging.enabled && hasValidBotConfig();
    }

    public boolean hasValidBotConfig() {
        return bot.token != null && !bot.token.isEmpty();
    }

    public boolean hasValidGroupConfig() {
        return group.id != null && !group.id.isEmpty();
    }

    public boolean hasTopicId() {
        return group.topicId != null && !group.topicId.isEmpty();
    }

    public Integer getTopicIdAsInteger() {
        if (!hasTopicId()) {
            return null;
        }
        try {
            return Integer.parseInt(group.topicId);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public String[] getLoggingLevels() {
        return logging.level.split(",");
    }
}