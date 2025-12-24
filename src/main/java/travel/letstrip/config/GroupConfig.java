package travel.letstrip.config;

/**
 * Configuration class that holds specific details for a single Telegram group,
 * allowing the bot to send messages, photos, or documents to the correct destination
 * and potentially within a specific thread (topic).
 */
public class GroupConfig {
    private Long chatId;
    private Integer topicId;
    private String name;

    /**
     * Default constructor for creating an empty GroupConfig object.
     */
    public GroupConfig() {}

    /**
     * Constructs a GroupConfig object with all necessary parameters.
     *
     * @param chatId The unique numerical identifier of the Telegram chat or group.
     * @param topicId The unique numerical identifier of the message thread (topic) within the chat (may be null if not used).
     * @param name A custom, friendly name for the group (e.g., "Marketing_Alerts").
     */
    public GroupConfig(Long chatId, Integer topicId, String name) {
        this.chatId = chatId;
        this.topicId = topicId;
        this.name = name;
    }

    /**
     * Returns the unique ID of the Telegram chat or group.
     *
     * @return The chat ID.
     */
    public Long getChatId() { return chatId; }

    /**
     * Sets the unique ID of the Telegram chat or group.
     *
     * @param chatId The new chat ID.
     */
    public void setChatId(Long chatId) { this.chatId = chatId; }

    /**
     * Returns the optional ID of the topic/thread within the group.
     *
     * @return The topic ID, or {@code null} if not specified.
     */
    public Integer getTopicId() { return topicId; }

    /**
     * Sets the ID of the topic/thread within the group.
     *
     * @param topicId The new topic ID.
     */
    public void setTopicId(Integer topicId) { this.topicId = topicId; }

    /**
     * Returns the custom, friendly name assigned to this group configuration.
     *
     * @return The group name string.
     */
    public String getName() { return name; }

    /**
     * Sets the custom, friendly name for this group configuration.
     *
     * @param name The new group name.
     */
    public void setName(String name) { this.name = name; }

    /**
     * Checks if this group configuration includes a valid topic ID, indicating
     * messages should be sent to a specific thread within the group.
     *
     * @return {@code true} if {@code topicId} is present and greater than zero, {@code false} otherwise.
     */
    public boolean hasTopic() { return topicId != null && topicId > 0; }
}