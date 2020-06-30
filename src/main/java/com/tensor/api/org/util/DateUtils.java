package com.tensor.api.org.util;

import java.time.LocalDateTime;

/**
 * @author liaochuntao
 */
public class DateUtils {

    public static String now() {
        LocalDateTime localDate = LocalDateTime.now();
        StringBuilder sb = new StringBuilder();
        sb
                .append(localDate.getYear())
                .append("-")
                .append(zeroFill(localDate.getMonthValue()))
                .append("-")
                .append(zeroFill(localDate.getDayOfMonth()))
                .append(" ")
                .append(zeroFill(localDate.getHour()))
                .append(":")
                .append(zeroFill(localDate.getMinute()))
                .append(":")
                .append(zeroFill(localDate.getSecond()));
        return sb.toString();
    }

    private static String zeroFill(int i) {
        if (i < 10) {
            return "0" + i;
        }
        return "" + i;
    }

}
