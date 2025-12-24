package travel.letstrip.config;


import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import travel.letstrip.service.TelegramBotService;

/**
 * Manages the lifecycle of the Telegram Bot.
 * <p>
 * This class is responsible for initializing the {@link TelegramBotsApi},
 * creating the {@link TelegramBotService}, and registering the bot to start
 * receiving updates from Telegram.
 */
public class TelegramBotManager {
    private TelegramBotService botService;
    private BotConfig config;

    /**
     * Constructs a new TelegramBotManager.
     *
     * @param config The bot configuration containing essential settings
     * (like token and username) and group details.
     */
    public TelegramBotManager(BotConfig config) {
        this.config = config;
    }

    /**
     * Initializes and starts the Telegram bot.
     * <p>
     * It registers the {@link TelegramBotService} instance with the
     * {@link TelegramBotsApi} to establish a long polling connection
     * and begin handling updates.
     *
     * @throws TelegramApiException If an error occurs during the bot registration or API setup.
     */
    public void start() throws TelegramApiException {
        // Initializes the TelegramBotsApi using the default session for long polling.
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);

        // Creates the main bot service implementation.
        botService = new TelegramBotService(config);

        // Registers the bot to start receiving updates.
        botsApi.registerBot(botService);
        System.out.println("Bot is running : " + config.getBotUsername());
    }

    /**
     * Retrieves the running instance of the {@link TelegramBotService}.
     *
     * @return The {@link TelegramBotService} instance, which handles bot logic.
     */
    public TelegramBotService getBotService() {
        return botService;
    }

    /**
     * Retrieves the bot's configuration object.
     *
     * @return The {@link BotConfig} object used to initialize the bot.
     */
    public BotConfig getConfig() {
        return config;
    }
}