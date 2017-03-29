package me.junbin.gradprj.util;

import me.junbin.commons.util.Jsr310Utils;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalQuery;
import java.util.Date;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/3/12 15:49
 * @description : {@link java.time.Instant} 对象必须先转成 {@link java.time.ZonedDateTime}
 * 之后才能进行其他操作
 */
public class Jsr310UtilExt extends Jsr310Utils {

    public static boolean hoursAgo(LocalDateTime dateTime, int hours) {
        return dateTime.plusHours(hours).isAfter(LocalDateTime.now());
    }

    public static boolean oneHourAgo(LocalDateTime dateTime) {
        return hoursAgo(dateTime, 1);
    }

    public static boolean twoHoursAgo(LocalDateTime dateTime) {
        return hoursAgo(dateTime, 2);
    }

    public static boolean threeHoursAgo(LocalDateTime dateTime) {
        return hoursAgo(dateTime, 3);
    }

    public static boolean oneDayAgo(LocalDateTime dateTime) {
        return dateTime.plusDays(1).isAfter(LocalDateTime.now());
    }

    public static <T> T parse(final String temporal, final TemporalQuery<T> query, final String firstPattern, String... moreCandidatePatterns) {
        try {
            return getFormatter(firstPattern).parse(temporal, query);
        } catch (DateTimeException e) {
            for (String pattern : moreCandidatePatterns) {
                try {
                    return getFormatter(pattern).parse(temporal, query);
                } catch (DateTimeException ignored) {
                }
            }
            throw new IllegalArgumentException(String.format("没有任何一个时间模式匹配指定的时间字符串%s", temporal));
        }
    }

    public static LocalDateTime parseDateTime(final String temporal, final String pattern, String... moreCandidatePatterns) {
        return parse(temporal, LocalDateTime::from, pattern, moreCandidatePatterns);
    }

    public static LocalDate parseDate(final String temporal, final String pattern, String... moreCandidatePatterns) {
        return parse(temporal, LocalDate::from, pattern, moreCandidatePatterns);
    }

    public static LocalTime parseTime(final String temporal, final String pattern, String... moreCandidatePatterns) {
        return parse(temporal, LocalTime::from, pattern, moreCandidatePatterns);
    }

    public static String format(final Date temporal, final String pattern) {
        return getFormatter(pattern).format(toZonedDateTime(temporal));
    }

}
