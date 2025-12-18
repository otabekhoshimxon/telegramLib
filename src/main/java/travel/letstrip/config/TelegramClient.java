package travel.letstrip.config;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import org.springframework.stereotype.Component;
import travel.letstrip.telegram.TelegramProperties;

import java.io.File;

/**
 * Telegram client wrapper for sending messages, documents, and photos.
 *
 * <p>This class provides a simplified API on top of
 * {@link com.pengrad.telegrambot.TelegramBot} and is configured using
 * {@link TelegramProperties}.</p>
 *
 * <p>Supported features:</p>
 * <ul>
 *   <li>Send text messages</li>
 *   <li>Send documents</li>
 *   <li>Send photos</li>
 *   <li>Support for Telegram topics (forum threads)</li>
 *   <li>Separate main, report, and error groups</li>
 * </ul>
 *
 * <p>All messages are sent using {@link ParseMode#Markdown}.</p>
 */
@Component
public class TelegramClient {

    private final TelegramBot bot;
    private final TelegramProperties properties;

    /**
     * Creates a new {@code TelegramClient} instance.
     *
     * @param properties Telegram configuration properties containing bot token,
     *                   group identifiers, and message settings
     */
    public TelegramClient(TelegramProperties properties) {
        this.properties = properties;
        this.bot = new TelegramBot(properties.getBot().getToken());
    }

    /* ================= PUBLIC API ================= */

    /**
     * Sends a document to the main Telegram group.
     *
     * @param file    document file to send
     * @param caption optional document caption
     */
    public void sendDocumentToMainGroup(File file, String caption) {
        sendDocument(properties.getGroup().getId(), file, caption, properties.getTopicIdAsInteger());
    }

    /**
     * Sends a photo to the main Telegram group.
     *
     * @param file    image file to send
     * @param caption optional photo caption
     */
    public void sendPhotoToMainGroup(File file, String caption) {
        sendPhoto(properties.getGroup().getId(), file, caption, properties.getTopicIdAsInteger());
    }

    /**
     * Sends a document to the error Telegram group.
     *
     * @param file    document file to send
     * @param caption optional document caption
     */
    public void sendDocumentToErrorGroup(File file, String caption) {
        sendDocument(properties.getGroup().getErrorGroupId(), file, caption, properties.getTopicIdAsInteger());
    }

    /**
     * Sends a text message to the main Telegram group.
     *
     * @param message message text
     */
    public void sendToMainGroup(String message) {
        send(properties.getGroup().getId(), message, properties.getTopicIdAsInteger());
    }

    /**
     * Sends a text message to the report Telegram group.
     *
     * @param message message text
     */
    public void sendToReportGroup(String message) {
        send(properties.getGroup().getReportGroupId(), message, properties.getGroup().getReportGroupTopicId());
    }

    /**
     * Sends a text message to the error Telegram group.
     *
     * @param message message text
     */
    public void sendToErrorGroup(String message) {
        send(properties.getGroup().getErrorGroupId(), message, properties.getTopicIdAsInteger());
    }

    /* ================= CORE METHODS ================= */

    /**
     * Sends a photo to the specified chat.
     *
     * @param chatId  Telegram chat ID
     * @param file    image file to send
     * @param caption optional caption
     * @param topicId optional topic (thread) ID
     */
    private void sendPhoto(String chatId, File file, String caption, Integer topicId) {
        if (!isValid(chatId, file)) return;

        SendPhoto request = new SendPhoto(chatId, file)
                .caption(caption)
                .parseMode(ParseMode.Markdown);

        if (topicId != null) {
            request.replyToMessageId(topicId);
        }

        bot.execute(request);
    }

    /**
     * Sends a document to the specified chat.
     *
     * @param chatId  Telegram chat ID
     * @param file    document file to send
     * @param caption optional caption
     * @param topicId optional topic (thread) ID
     */
    private void sendDocument(String chatId, File file, String caption, Integer topicId) {
        if (!isValid(chatId, file)) return;

        SendDocument request = new SendDocument(chatId, file)
                .caption(caption)
                .parseMode(ParseMode.Markdown);

        if (topicId != null) {
            request.replyToMessageId(topicId);
        }

        bot.execute(request);
    }

    /**
     * Sends a text message to the specified chat.
     *
     * @param chatId  Telegram chat ID
     * @param message message text
     * @param topicId optional topic (thread) ID
     */
    private void send(String chatId, String message, Integer topicId) {
        if (chatId == null || chatId.isBlank()) {
            return;
        }

        SendMessage request = new SendMessage(chatId, message)
                .parseMode(ParseMode.Markdown)
                .disableWebPagePreview(properties.getMessage().isDisableWebPagePreview())
                .disableNotification(properties.getMessage().isDisableNotification());

        if (topicId != null) {
            request.replyToMessageId(topicId);
        }

        bot.execute(request);
    }

    /* ================= HELPERS ================= */

    /**
     * Validates chat ID and file existence before sending media.
     *
     * @param chatId Telegram chat ID
     * @param file   file to send
     * @return {@code true} if parameters are valid, otherwise {@code false}
     */
    private boolean isValid(String chatId, File file) {
        return chatId != null && !chatId.isBlank()
                && file != null && file.exists();
    }

}
