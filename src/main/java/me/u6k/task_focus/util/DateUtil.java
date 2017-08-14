
package me.u6k.task_focus.util;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

public final class DateUtil {

    private static final String FORMAT_FULL_DATETIME = "yyyy-MM-dd HH:mm:ss.SSS";

    private DateUtil() {
    }

    public static Date datetimeToDate(Date date) {
        Date newDate = DateUtils.setHours(date, 0);
        newDate = DateUtils.setMinutes(newDate, 0);
        newDate = DateUtils.setSeconds(newDate, 0);
        newDate = DateUtils.setMilliseconds(newDate, 0);

        return newDate;
    }

    public static Date hourMinutesToDatetime(Date date) {
        Date newDate = new Date();
        Calendar calendar = DateUtils.toCalendar(date);

        newDate = DateUtils.setHours(newDate, calendar.get(Calendar.HOUR_OF_DAY));
        newDate = DateUtils.setMinutes(newDate, calendar.get(Calendar.MINUTE));
        newDate = DateUtils.setSeconds(newDate, 0);
        newDate = DateUtils.setMilliseconds(newDate, 0);

        return newDate;
    }

    public static Date parseFullDatetime(String str) throws ParseException {
        Date date = DateUtils.parseDate(str, FORMAT_FULL_DATETIME);

        return date;
    }

}
