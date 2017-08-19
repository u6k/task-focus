
package me.u6k.task_focus.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public final class DateUtil {

    private static final String FORMAT_FULL_DATETIME = "yyyy-MM-dd HH:mm:ss.SSS";

    private static final String FORMAT_DATE = "yyyy-MM-dd";

    private static final String FORMAT_HOUR_MINUTE = "HH:mm";

    private DateUtil() {
    }

    public static Date parseFullDatetime(String str) {
        SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_FULL_DATETIME);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date;
        try {
            date = formatter.parse(str);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return date;
    }

    public static String formatDate(Date date) {
        return format(date, FORMAT_DATE);
    }

    public static String formatHourMinute(Date date) {
        return format(date, FORMAT_HOUR_MINUTE);
    }

    public static Date toDatetime(Date datePart, Date timePart) {
        if (timePart == null) {
            return null;
        }

        String str = formatDate(datePart) + " " + formatHourMinute(timePart) + ":00.000";
        Date date = parseFullDatetime(str);

        return date;
    }

    private static String format(Date date, String format) {
        if (date == null) {
            return null;
        }

        String str = new SimpleDateFormat(format).format(date);

        return str;
    }

}
