package travel.letstrip.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration class that holds all necessary parameters for the Telegram Bot,
 * including authentication credentials and a list of managed groups.
 */
public class BotConfig {
    private String botToken;
    private String botUsername;
    private List<GroupConfig> groups;

    /**
     * Default constructor. Initializes the internal list of groups as an empty ArrayList.
     */
    public BotConfig() {
        this.groups = new ArrayList<>();
    }

    /**
     * Constructor used to initialize the core bot credentials.
     *
     * @param botToken The authentication token provided by BotFather.
     * @param botUsername The unique username of the bot (e.g., "@MyBot").
     */
    public BotConfig(String botToken, String botUsername) {
        this.botToken = botToken;
        this.botUsername = botUsername;
        this.groups = new ArrayList<>();
    }

    /**
     * Returns the Telegram bot's authentication token.
     *
     * @return The bot token string.
     */
    public String getBotToken() { return botToken; }

    /**
     * Sets the Telegram bot's authentication token.
     *
     * @param botToken The new bot token string.
     */
    public void setBotToken(String botToken) { this.botToken = botToken; }

    /**
     * Returns the Telegram bot's username.
     *
     * @return The bot username string.
     */
    public String getBotUsername() { return botUsername; }

    /**
     * Sets the Telegram bot's username.
     *
     * @param botUsername The new bot username string.
     */
    public void setBotUsername(String botUsername) { this.botUsername = botUsername; }

    /**
     * Returns the list of configured groups managed by this bot.
     *
     * @return A {@code List<GroupConfig>} containing details for each managed group.
     */
    public List<GroupConfig> getGroups() { return groups; }

    /**
     * Sets the entire list of configured groups.
     *
     * @param groups The new list of {@link GroupConfig} objects.
     */
    public void setGroups(List<GroupConfig> groups) { this.groups = groups; }

    /**
     * Adds a single group configuration to the list of managed groups.
     *
     * @param group The {@link GroupConfig} object to add.
     */
    public void addGroup(GroupConfig group) { this.groups.add(group); }

    /**
     * Finds a configured group by its custom name using a stream and filter.
     *
     * @param name The name of the group to search for.
     * @return The matching {@link GroupConfig} object, or {@code null} if not found.
     */
    public GroupConfig getGroupByName(String name) {
        return groups.stream()
                .filter(g -> g.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Finds a configured group by its Telegram chat ID using a stream and filter.
     *
     * @param id The chat ID (Long) of the group to search for.
     * @return The matching {@link GroupConfig} object, or {@code null} if not found.
     */
    public GroupConfig getGroupById(Long id) {
        return groups.stream()
                .filter(g -> g.getChatId().equals(id))
                .findFirst()
                .orElse(null);
    }
}