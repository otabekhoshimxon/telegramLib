package travel.letstrip.telegram;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.AppenderBase;
import lombok.Setter;
import travel.letstrip.config.TelegramClient;

import java.time.Instant;
import java.util.Set;

public class TelegramLogbackAppender extends AppenderBase<ILoggingEvent> {

    @Setter
    private TelegramClient telegramClient;
    private TelegramProperties properties;
    private Set<String> allowedLevels;

    /* ==== Spring orqali set qilinadi ==== */

    public void setProperties(TelegramProperties properties) {
        this.properties = properties;
        this.allowedLevels = Set.of(properties.getLoggingLevels());
    }

    @Override
    protected void append(ILoggingEvent event) {
        if (!isStarted() || telegramClient == null || properties == null) {
            return;
        }

        if (!properties.isLoggingEnabled()) {
            return;
        }

        if (!allowedLevels.contains(event.getLevel().levelStr)) {
            return;
        }

        String message = buildMessage(event);
        telegramClient.sendToErrorGroup(message);
    }

    /* ================= MESSAGE BUILDER ================= */

    private String buildMessage(ILoggingEvent event) {
        StringBuilder sb = new StringBuilder();

        sb.append("🚨 <b>").append(event.getLevel()).append("</b>\n");
        sb.append("📦 <b>").append(event.getLoggerName()).append("</b>\n");
        sb.append("🕒 ").append(Instant.ofEpochMilli(event.getTimeStamp())).append("\n\n");

        sb.append("<pre>").append(escape(event.getFormattedMessage())).append("</pre>");

        if (properties.getLogging().isIncludeStackTrace() && event.getThrowableProxy() != null) {
            sb.append("\n\n").append(buildStackTrace(event.getThrowableProxy()));
        }

        return sb.toString();
    }

    private String buildStackTrace(IThrowableProxy throwable) {
        StringBuilder sb = new StringBuilder();
        sb.append("<b>Stacktrace:</b>\n<pre>");

        StackTraceElementProxy[] elements = throwable.getStackTraceElementProxyArray();
        int limit = Math.min(elements.length, properties.getLogging().getMaxStackTraceLines());

        for (int i = 0; i < limit; i++) {
            sb.append(elements[i].getStackTraceElement()).append("\n");
        }

        sb.append("</pre>");
        return sb.toString();
    }

    private String escape(String text) {
        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}
