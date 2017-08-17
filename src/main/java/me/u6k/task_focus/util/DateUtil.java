
package me.u6k.task_focus.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

public final class DateUtil {

    private static final String FORMAT_DATE = "yyyy-MM-dd";

    private static final String FORMAT_HOUR_MINUTE = "HH:mm";

    private DateUtil() {
    }

    public static Date toDate(int year, int month, int day, int hour, int minute, int second, int millisecond) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, second);
        c.set(Calendar.MILLISECOND, millisecond);

        return c.getTime();
    }

    public static Date toDate(int year, int month, int day) {
        return toDate(year, month, day, 0, 0, 0, 0);
    }

    public static Date toDate(int hour, int minute) {
        Calendar c = Calendar.getInstance();

        return toDate(c.get(Calendar.YEAR),
            c.get(Calendar.MONTH),
            c.get(Calendar.DAY_OF_MONTH),
            hour,
            minute,
            c.get(Calendar.SECOND),
            c.get(Calendar.MILLISECOND));
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
