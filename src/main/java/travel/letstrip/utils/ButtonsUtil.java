package travel.letstrip.utils;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility class for easily creating and configuring Telegram Inline and Reply Keyboards.
 * <p>
 * Primarily focuses on building Inline Keyboards row by row.
 */
public class ButtonsUtil {

    private ButtonsUtil() {
        // Statik klass bo'lgani uchun konstruktor yopiq
    }

    // --- Inline Keyboard (Callback/URL tugmalar) Yaratuvchi (Builder) qismi ---

    /**
     * Creates a single InlineKeyboardButton with text and callback data.
     *
     * @param text The text displayed on the button.
     * @param callbackData The data sent to the bot when the button is pressed.
     * @return An InlineKeyboardButton instance.
     */
    public static InlineKeyboardButton createInlineButton(String text, String callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);
        return button;
    }

    /**
     * Creates a single InlineKeyboardButton that opens a URL.
     *
     * @param text The text displayed on the button.
     * @param url The URL to open when the button is pressed.
     * @return An InlineKeyboardButton instance.
     */
    public static InlineKeyboardButton createUrlButton(String text, String url) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setUrl(url);
        return button;
    }

    /**
     * Creates an InlineKeyboardMarkup from a single list of buttons, placing them
     * all in one row.
     *
     * @param buttons A list of InlineKeyboardButton objects.
     * @return The resulting InlineKeyboardMarkup.
     */
    public static InlineKeyboardMarkup createMarkupFromList(List<InlineKeyboardButton> buttons) {
        List<List<InlineKeyboardButton>> rowList = Collections.singletonList(buttons);
        return createMarkup(rowList);
    }

    /**
     * Creates an InlineKeyboardMarkup from a list of button rows.
     * This is the core method for constructing an Inline Keyboard.
     *
     * @param keyboard A list of button rows (where each row is a list of buttons).
     * @return The final InlineKeyboardMarkup object.
     */
    public static InlineKeyboardMarkup createMarkup(List<List<InlineKeyboardButton>> keyboard) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(keyboard);
        return markup;
    }

    // --- Reply Keyboard (Oddiy matn tugmalar) Yaratuvchi qismi ---

    /**
     * Creates a single KeyboardButton with simple text.
     *
     * @param text The text that will be sent as a message when the button is pressed.
     * @return A KeyboardButton instance.
     */
    public static KeyboardButton createReplyButton(String text) {
        return new KeyboardButton(text);
    }

    /**
     * Creates a KeyboardButton that requests the user's contact information.
     *
     * @param text The text displayed on the button (e.g., "Share Contact").
     * @return A KeyboardButton instance configured to request contact.
     */
    public static KeyboardButton createContactButton(String text) {
        KeyboardButton button = new KeyboardButton(text);
        button.setRequestContact(true);
        return button;
    }

    /**
     * Creates a ReplyKeyboardMarkup from a list of simple text buttons, arranged in a single column.
     *
     * @param buttons A list of button texts.
     * @return The resulting ReplyKeyboardMarkup.
     */
    public static ReplyKeyboardMarkup createReplyMarkupSingleColumn(List<String> buttons) {
        List<KeyboardRow> keyboard = new ArrayList<>();
        for (String text : buttons) {
            KeyboardRow row = new KeyboardRow();
            row.add(createReplyButton(text));
            keyboard.add(row);
        }
        return createReplyMarkup(keyboard, true, true);
    }

    /**
     * Creates a ReplyKeyboardMarkup from a list of KeyboardRow objects.
     *
     * @param keyboard The list of KeyboardRow objects.
     * @param resizeKeyboard If true, the keyboard is resized to fit the screen better.
     * @param oneTimeKeyboard If true, the keyboard is hidden after use.
     * @return The final ReplyKeyboardMarkup object.
     */
    public static ReplyKeyboardMarkup createReplyMarkup(List<KeyboardRow> keyboard, boolean resizeKeyboard, boolean oneTimeKeyboard) {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setKeyboard(keyboard);
        markup.setResizeKeyboard(resizeKeyboard);
        markup.setOneTimeKeyboard(oneTimeKeyboard);
        return markup;
    }
}