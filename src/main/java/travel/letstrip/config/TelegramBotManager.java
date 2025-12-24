package travel.letstrip.config;


import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class TelegramBotManager {
    private TelegramBotService botService;
    private BotConfig config;
    
    public TelegramBotManager(BotConfig config) {
        this.config = config;
    }
    
    public void start() throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botService = new TelegramBotService(config);
        botsApi.registerBot(botService);
        System.out.println("Bot ishga tushdi: " + config.getBotUsername());
    }
    
    public TelegramBotService getBotService() {
        return botService;
    }
    
    public BotConfig getConfig() {
        return config;
    }
}