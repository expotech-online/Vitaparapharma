package org.ahmedukamel.ecommerce.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {
    public static final String DATE_TIME_PATTERN = "yyyy-MM-ddTHH:mm:ss";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

    public static LocalDateTime getDateTime(String time) {
        return LocalDateTime.parse(time, formatter);
    }
}
