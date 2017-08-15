
package me.u6k.task_focus.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.apache.commons.lang3.time.DateUtils;

public final class DateUtil {

    private static final String FORMAT_FULL_DATETIME = "yyyy-MM-dd HH:mm:ss.SSS";

    private static final String FORMAT_DATE = "yyyy-MM-dd";

    private static final String FORMAT_HOUR_MINUTE = "HH:mm";

    private DateUtil() {
    }

    public static Optional<Date> parseFullDatetime(Optional<String> str) throws ParseException {
        return parse(str, FORMAT_FULL_DATETIME);
    }

    public static Optional<Date> parseDate(Optional<String> str) throws ParseException {
        return parse(str, FORMAT_DATE);
    }

    public static Optional<Date> parseHourMinute(Optional<String> str) throws ParseException {
        return parse(str, FORMAT_HOUR_MINUTE);
    }

    public static Optional<String> formatDate(Optional<Date> date) {
        return format(date, FORMAT_DATE);
    }

    public static Optional<String> formatHourMinute(Optional<Date> date) {
        return format(date, FORMAT_HOUR_MINUTE);
    }

    private static Optional<Date> parse(Optional<String> str, String format) throws ParseException {
        if (!str.isPresent()) {
            return Optional.empty();
        }

        Optional<Date> date = Optional.of(DateUtils.parseDate(str.get(), format));

        return date;
    }

    private static Optional<String> format(Optional<Date> date, String format) {
        Optional<String> str = date.map(x -> new SimpleDateFormat(format).format(x));

        return str;
    }

}
