package travel.letstrip.service;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.groupadministration.BanChatMember;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import travel.letstrip.config.BotConfig;
import travel.letstrip.config.GroupConfig;

import java.io.File;
import java.util.*;

/**
 * Service class that extends {@link TelegramLongPollingBot} to handle
 * Telegram bot interactions, including receiving updates and sending messages,
 * photos, and documents. It manages bot configuration and provides utility
 * methods for interacting with Telegram groups.
 * <p>
 * This bot is configured to use a {@link BotConfig} object for its settings.
 */
@Slf4j
public class TelegramBotService extends TelegramLongPollingBot {
    private final BotConfig config;
    private final Map<Long, Set<Long>> groupMembers = new HashMap<>();

    /**
     * Constructs a new TelegramBotService.
     *
     * @param config The bot configuration containing the bot token, username,
     * and details for managed groups.
     */
    public TelegramBotService(BotConfig config) {
        this.config = config;
    }

    /**
     * Retrieves the bot's username as defined in the configuration.
     *
     * @return The bot's username (e.g., "MyAwesomeBot").
     */
    @Override
    public String getBotUsername() {
        return config.getBotUsername();
    }

    /**
     * Retrieves the bot's token as defined in the configuration.
     *
     * @return The bot's authentication token.
     */
    @Override
    public String getBotToken() {
        return config.getBotToken();
    }

    /**
     * This method is called when an update is received from the Telegram API.
     * It currently handles incoming text messages, new chat members, and
     * members leaving the chat by logging the events to the console.
     *
     * @param update The incoming update object from Telegram.
     */
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            System.out.println("Message received: " + text + " from " + chatId);
        }
        if (update.hasMessage()) {
            if (update.getMessage().getNewChatMembers() != null) {
                update.getMessage().getNewChatMembers().forEach(user -> {
                    System.out.println("New member: " + user.getUserName() + " (" + user.getId() + ")");
                    // Possible to save to DB
                });
            }

            if (update.getMessage().getLeftChatMember() != null) {
                System.out.println("Member left: " + update.getMessage().getLeftChatMember().getUserName());
            }
        }
    }

    /**
     * Fetches the detailed information about a specific member in a chat/group.
     * This uses the {@link GetChatMember} API method.
     *
     * @param chatId The ID of the chat or group.
     * @param userId The ID of the user to check.
     * @return The {@link ChatMember} object if successful, or {@code null} if an
     * exception occurs (logged to error).
     */
    public ChatMember getChatMember(Long chatId, Long userId) {
        try {
            GetChatMember getChatMember = new GetChatMember();
            getChatMember.setChatId(chatId.toString());
            getChatMember.setUserId(userId); // userId should generally be Long, intValue cast is risky
            return execute(getChatMember);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * Adds a user's ID to the internal set of members for a specific group chat ID.
     *
     * @param chatId The ID of the group chat.
     * @param userId The ID of the user to add.
     */
    private void addMember(Long chatId, Long userId) {
        groupMembers.computeIfAbsent(chatId, k -> new HashSet<>()).add(userId);
    }

    /**
     * Retrieves the set of user IDs currently tracked as members for a given group.
     * Returns an empty set if the group is not tracked.
     *
     * @param chatId The ID of the group chat.
     * @return A {@code Set<Long>} of user IDs who are members of the group.
     */
    public Set<Long> getMembers(Long chatId) {
        return groupMembers.getOrDefault(chatId, Collections.emptySet());
    }

    /**
     * Removes a user's ID from the internal set of members for a specific group chat ID.
     *
     * @param chatId The ID of the group chat.
     * @param userId The ID of the user to remove.
     */
    private void removeMember(Long chatId, Long userId) {
        Set<Long> members = groupMembers.get(chatId);
        if (members != null) {
            members.remove(userId);
        }
    }

    /**
     * Sends a text message to all groups defined in the bot configuration.
     *
     * @param message The text message to be sent.
     * @return A list of strings indicating the success status for each group.
     */
    public List<String> sendToAllGroups(String message) {
        List<String> results = new ArrayList<>();
        for (GroupConfig group : config.getGroups()) {
            sendMessage(group, message);
            results.add("✓ " + group.getName() + " - send");
        }
        return results;
    }

    // --- YANGI METODLAR ---

    /**
     * Helper method to execute a SendMessage object.
     *
     * @param sendMessage The configured SendMessage object.
     * @return {@code true} if successful, {@code false} otherwise.
     */
    private boolean sendActionMessage(SendMessage sendMessage) {
        try {
            execute(sendMessage);
            return true;
        } catch (TelegramApiException e) {
            log.error("Failed to send message to chat ID {}: {}", sendMessage.getChatId(), e.getMessage());
            return false;
        }
    }

    /**
     * Sends a basic text message to a specific Telegram group defined by its configuration.
     *
     * @param group   The {@link GroupConfig} object containing the group ID and topic ID (if applicable).
     * @param message The text message to send.
     * @return {@code true} if the message was sent successfully, {@code false} otherwise.
     */
    public boolean sendMessage(GroupConfig group, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(group.getChatId().toString());
        sendMessage.setText(message);

        if (group.hasTopic()) {
            sendMessage.setMessageThreadId(group.getTopicId());
        }

        return sendActionMessage(sendMessage);
    }

    /**
     * Sends a text message to a specific Telegram group, allowing for a custom {@link ParseMode}
     * (e.g., Markdown or HTML).
     *
     * @param group     The {@link GroupConfig} object containing the group ID and topic ID (if applicable).
     * @param message   The text message to send.
     * @param parseMode The {@link ParseMode} to use for formatting the message text.
     * @return {@code true} if the message was sent successfully, {@code false} otherwise.
     */
    public boolean sendMessage(GroupConfig group, String message, ParseMode parseMode) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(group.getChatId().toString());
        sendMessage.setText(message);
        sendMessage.setParseMode(parseMode.toString());

        if (group.hasTopic()) {
            sendMessage.setMessageThreadId(group.getTopicId());
        }

        return sendActionMessage(sendMessage);
    }

    /**
     * Sends a message in reply to a specific existing message in a chat.
     *
     * @param chatId The ID of the chat where the message should be sent.
     * @param replyToMessageId The ID of the message to which the bot is replying.
     * @param text The text message content.
     * @param parseMode The {@link ParseMode} for formatting.
     * @return {@code true} if the message was sent successfully, {@code false} otherwise.
     */
    public boolean sendReplyMessage(Long chatId, Integer replyToMessageId, String text, ParseMode parseMode) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(text);
        sendMessage.setReplyToMessageId(replyToMessageId);

        if (parseMode != null) {
            sendMessage.setParseMode(parseMode.toString());
        }

        return sendActionMessage(sendMessage);
    }

    /**
     * Sends a text message to a group looked up by its registered name in the configuration.
     *
     * @param groupName The name of the group as defined in the configuration.
     * @param message   The text message to send.
     * @return {@code true} if the message was sent successfully, {@code false} otherwise.
     * @throws IllegalArgumentException if the group name is not found in the configuration.
     */
    public boolean sendMessageByGroupName(String groupName, String message) {
        GroupConfig group = config.getGroupByName(groupName);
        if (group == null) {
            throw new IllegalArgumentException("Group not found : " + groupName);
        }
        return sendMessage(group, message);
    }

    /**
     * Sends a text message to a group looked up by its ID in the configuration.
     *
     * @param groupId The ID of the group as defined in the configuration.
     * @param message The text message to send.
     * @return {@code true} if the message was sent successfully, {@code false} otherwise.
     * @throws IllegalArgumentException if the group ID is not found in the configuration.
     */
    public boolean sendMessageByGroupId(Long groupId, String message) {
        GroupConfig group = config.getGroupById(groupId);
        if (group == null) {
            throw new IllegalArgumentException("Group not found by id : " + groupId);
        }
        return sendMessage(group, message);
    }

    /**
     * Sends a photo file to a specific Telegram group with an optional caption.
     *
     * @param group   The {@link GroupConfig} object for the target group.
     * @param photo   The {@link File} object representing the photo to send.
     * @param caption An optional caption for the photo (can be {@code null} or empty).
     * @return {@code true} if the photo was sent successfully, {@code false} otherwise.
     */
    public boolean sendPhoto(GroupConfig group, File photo, String caption) {
        try {
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(group.getChatId().toString());
            sendPhoto.setPhoto(new InputFile(photo));

            if (caption != null && !caption.isEmpty()) {
                sendPhoto.setCaption(caption);
            }

            if (group.hasTopic()) {
                sendPhoto.setMessageThreadId(group.getTopicId());
            }

            execute(sendPhoto);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * Sends a photo file to a group looked up by its ID in the configuration.
     *
     * @param groupId The ID of the group as defined in the configuration.
     * @param photo The {@link File} object representing the photo to send.
     * @param caption An optional caption for the photo.
     * @return {@code true} if the photo was sent successfully, {@code false} otherwise.
     */
    public boolean sendPhotoByGroupId(Long groupId, File photo, String caption) {
        GroupConfig group = config.getGroupById(groupId);
        if (group == null) {
            throw new IllegalArgumentException("Group not found by id : " + groupId);
        }
        return sendPhoto(group, photo, caption);
    }

    /**
     * Sends a document file (e.g., a PDF, text file, etc.) to a specific Telegram group
     * with an optional caption.
     *
     * @param group    The {@link GroupConfig} object for the target group.
     * @param document The {@link File} object representing the document to send.
     * @param caption  An optional caption for the document (can be {@code null} or empty).
     * @return {@code true} if the document was sent successfully, {@code false} otherwise.
     */
    public boolean sendDocument(GroupConfig group, File document, String caption) {
        try {
            SendDocument sendDocument = new SendDocument();
            sendDocument.setChatId(group.getChatId().toString());
            sendDocument.setDocument(new InputFile(document));

            if (caption != null && !caption.isEmpty()) {
                sendDocument.setCaption(caption);
            }

            if (group.hasTopic()) {
                sendDocument.setMessageThreadId(group.getTopicId());
            }

            execute(sendDocument);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
            return false;
        }

        return true;
    }

    // --- CHAT ADMINISTRATION METHODS ---

    /**
     * Kicks a member from a chat (equivalent to 'unban' in Telegram API, allowing the user to rejoin).
     * Note: This method requires the bot to be an administrator in the chat with the
     * 'Restrict and ban users' permission.
     *
     * @param chatId The ID of the chat/group.
     * @param userId The ID of the user to kick.
     * @return {@code true} if the member was successfully kicked, {@code false} otherwise.
     */
    public boolean kickChatMember(Long chatId, Long userId) {
        BanChatMember kickChatMember = new BanChatMember();
        kickChatMember.setChatId(chatId.toString());
        kickChatMember.setUserId(userId);

        // Setting untilDate to current time + 1 second effectively kicks them instantly,
        // allowing them to rejoin if the chat is public.
        // For a true ban, remove the untilDate setting.
        kickChatMember.setUntilDate((int) (System.currentTimeMillis() / 1000) + 1);

        try {
            execute(kickChatMember);
            return true;
        } catch (TelegramApiException e) {
            log.error("Failed to kick user {} from chat {}: {}", userId, chatId, e.getMessage());
            return false;
        }
    }

    /**
     * Bans a member from a chat permanently (or for a specified duration).
     * Note: Requires 'Restrict and ban users' administrator permission.
     * By setting untilDate to null (or simply omitting it), the ban is permanent.
     *
     * @param chatId The ID of the chat/group.
     * @param userId The ID of the user to ban.
     * @param untilDateUnix Optional: Date when the user can return, as a Unix timestamp (seconds).
     * If {@code null} or 0, the ban is permanent.
     * @return {@code true} if the member was successfully banned, {@code false} otherwise.
     */
    public boolean banChatMember(Long chatId, Long userId, Integer untilDateUnix) {
        BanChatMember banChatMember = new BanChatMember();
        banChatMember.setChatId(chatId.toString());
        banChatMember.setUserId(userId);

        if (untilDateUnix != null && untilDateUnix > 0) {
            banChatMember.setUntilDate(untilDateUnix);
        }

        try {
            execute(banChatMember);
            return true;
        } catch (TelegramApiException e) {
            log.error("Failed to ban user {} from chat {}: {}", userId, chatId, e.getMessage());
            return false;
        }
    }
}