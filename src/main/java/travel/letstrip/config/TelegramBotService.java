package travel.letstrip.config;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TelegramBotService extends TelegramLongPollingBot {
    private final BotConfig config;

    public TelegramBotService(BotConfig config) {
        this.config = config;
    }

    @Override
    public String getBotUsername() {
        return config.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            System.out.println("Xabar olindi: " + text + " from " + chatId);
        }
    }

    public List<String> sendToAllGroups(String message) {
        List<String> results = new ArrayList<>();
        for (GroupConfig group : config.getGroups()) {
            try {
                sendMessage(group, message);
                results.add("✓ " + group.getName() + " - yuborildi");
            } catch (TelegramApiException e) {
                results.add("✗ " + group.getName() + " - xato: " + e.getMessage());
            }
        }
        return results;
    }
    public boolean sendMessage(GroupConfig group, String message) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(group.getChatId().toString());
        sendMessage.setText(message);

        if (group.hasTopic()) {
            sendMessage.setMessageThreadId(group.getTopicId());
        }

        execute(sendMessage);
        return true;
    }

    public boolean sendMessageByGroupName(String groupName, String message) throws TelegramApiException {
        GroupConfig group = config.getGroupByName(groupName);
        if (group == null) {
            throw new IllegalArgumentException("Guruh topilmadi: " + groupName);
        }
        return sendMessage(group, message);
    }

    public boolean sendPhoto(GroupConfig group, File photo, String caption) throws TelegramApiException {
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
        return true;
    }

    public boolean sendDocument(GroupConfig group, File document, String caption) throws TelegramApiException {
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
        return true;
    }
}
