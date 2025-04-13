package org.apollo.mail.util;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    private static final SimpleDateFormat YM_FORMAT = new SimpleDateFormat("yyyyMM");
    private static final SimpleDateFormat DAY_FORMAT = new SimpleDateFormat("dd");
    private static final SimpleDateFormat HOUR_FORMAT = new SimpleDateFormat("HH");

    public static String formatYearMonth(Date date) {
        return date != null ? YM_FORMAT.format(date) : "";
    }

    public static String formatDay(Date date) {
        return date != null ? DAY_FORMAT.format(date) : "";
    }

    public static String formatHour(Date date) {
        return date != null ? HOUR_FORMAT.format(date) : "";
    }

    public static Date toDate(LocalDateTime localDateTime) {
        return localDateTime != null ? Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()) : null;
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        return date != null ? LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()) : null;
    }

    /**
     * Adds or subtracts the specified number of months to the given date.
     * @param date The date to modify.
     * @param months The number of months to add (negative to subtract).
     * @return The new date.
     */
    public static Date addMonths(Date date, int months) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, months);
        return cal.getTime();
    }
} 