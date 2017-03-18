package me.junbin.gradprj.util;

import me.junbin.commons.util.Jsr310Utils;

import java.time.LocalDateTime;

/**
 * @author : Zhong Junbin
 * @email : <a href="mailto:rekadowney@163.com">发送邮件</a>
 * @createDate : 2017/3/12 15:49
 * @description :
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

}
