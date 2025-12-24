package travel.letstrip.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Utility class for managing and manipulating Telegram Callback Query data strings.
 * It uses a simple delimiter (default: '|') to separate different parts of the data.
 * <p>
 * Format: COMMAND|ID|PAGE_NUMBER...
 */
public class CallbackDataUtil {

    private static final String DELIMITER = "|";

    private CallbackDataUtil() {
        // Statik klass
    }


    /**
     * Builds a single callback data string by joining multiple arguments with the delimiter.
     *
     * @param parts A list or array of strings to be joined. The first part is usually the command.
     * @return The combined callback data string.
     */
    public static String build(String... parts) {
        if (parts == null || parts.length == 0) {
            return "";
        }
        return String.join(DELIMITER, parts);
    }


    /**
     * Splits a callback data string into a list of its components based on the delimiter.
     *
     * @param callbackData The raw callback data string received from Telegram.
     * @return A List of strings representing the parts of the callback data.
     */
    public static List<String> parse(String callbackData) {
        if (callbackData == null || callbackData.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.asList(callbackData.split("\\" + DELIMITER));
    }

    /**
     * Extracts the main command (the first part) from the callback data.
     *
     * @param callbackData The raw callback data string.
     * @return The command string, or null if the data is empty.
     */
    public static String getCommand(String callbackData) {
        List<String> parts = parse(callbackData);
        return parts.isEmpty() ? null : parts.get(0);
    }

    /**
     * Extracts a specific part of the callback data by its index.
     *
     * @param callbackData The raw callback data string.
     * @param index The zero-based index of the part to retrieve (0 is command, 1 is first argument, etc.).
     * @return The string part at the specified index, or null if the index is out of bounds.
     */
    public static String getPart(String callbackData, int index) {
        List<String> parts = parse(callbackData);
        if (index >= 0 && index < parts.size()) {
            return parts.get(index);
        }
        return null;
    }
}