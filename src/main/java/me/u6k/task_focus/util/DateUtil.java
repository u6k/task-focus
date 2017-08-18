
package me.u6k.task_focus.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
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

    public static Optional<String> formatDate(Optional<Date> date) {
        return format(date, FORMAT_DATE);
    }

    public static Optional<String> formatHourMinute(Optional<Date> date) {
        return format(date, FORMAT_HOUR_MINUTE);
    }

    private static Optional<String> format(Optional<Date> date, String format) {
        Optional<String> str = date.map(x -> new SimpleDateFormat(format).format(x));

        return str;
    }

}
