package com.tensor.api.org.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author liaochuntao
 */
public final class IDUtils {

    private static final LongAdder ID_N = new LongAdder();

    public static Long buildId() {
        StringBuilder idBuilder = new StringBuilder();
        long last = ID_N.longValue();
        ID_N.increment();
        String currTime = String.valueOf(System.currentTimeMillis());
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();
        idBuilder
                .append(localDate.getYear())
                .append(zeroFill(localDate.getMonthValue()))
                .append(zeroFill(localDate.getDayOfMonth()))
                .append(zeroFill(localTime.getHour()))
                .append(currTime.substring(currTime.length() - 4))
                .append(last);
        if (last / 100 != 0) {
            ID_N.reset();
        }
        return Long.valueOf(idBuilder.toString());
    }

    private static String zeroFill(int n) {
        if (n / 10 == 0) {
            return "0" + n;
        }
        return "" + n;
    }

}
