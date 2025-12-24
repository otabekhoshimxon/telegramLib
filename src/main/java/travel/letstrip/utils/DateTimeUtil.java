package travel.letstrip.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for handling Date and Time operations using the modern Java Time API (java.time).
 * Provides methods for formatting, time zone conversion, and general date manipulation.
 */
public class DateTimeUtil {

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DISPLAY_DATETIME_FORMAT = "dd.MM.yyyy HH:mm";

    public static final ZoneId DEFAULT_ZONE = ZoneId.of("Asia/Tashkent");

    private DateTimeUtil() {
        // Statik klass
    }

    /**
     * Converts a Unix timestamp (seconds) into a formatted date/time string.
     *
     * @param unixSeconds The timestamp in seconds (often from Telegram Update.getMessage().getDate()).
     * @param format The desired date/time format pattern (e.g., DATETIME_FORMAT).
     * @param zoneId The time zone to use for conversion.
     * @return The formatted date/time string.
     */
    public static String formatUnixTimestamp(long unixSeconds, String format, ZoneId zoneId) {
        Instant instant = Instant.ofEpochSecond(unixSeconds);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return localDateTime.format(formatter);
    }

    /**
     * Formats the current time using the default time zone and a specified format.
     *
     * @param format The desired date/time format pattern.
     * @return The formatted string of the current time.
     */
    public static String formatCurrentTime(String format) {
        LocalDateTime now = LocalDateTime.now(DEFAULT_ZONE);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return now.format(formatter);
    }

    /**
     * Converts a LocalDateTime object to a string using the specified format.
     *
     * @param dateTime The LocalDateTime object to format.
     * @param format The desired format pattern.
     * @return The formatted string.
     */
    public static String formatDateTime(LocalDateTime dateTime, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return dateTime.format(formatter);
    }

    /**
     * Converts a LocalDateTime object from one time zone to another.
     *
     * @param localDateTime The original date and time.
     * @param originalZone The original time zone.
     * @param targetZone The target time zone.
     * @return The converted LocalDateTime in the target time zone.
     */
    public static LocalDateTime convertZone(LocalDateTime localDateTime, ZoneId originalZone, ZoneId targetZone) {
        return localDateTime.atZone(originalZone)
                .withZoneSameInstant(targetZone)
                .toLocalDateTime();
    }
}