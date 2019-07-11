package com.tensor.nacos.api.util;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author <a href="mailto:liaochunyhm@live.com">liaochuntao</a>
 * @since
 */
public class IDUtils {

    private static final AtomicLong LONG_ADDER = new AtomicLong(0);

    public static synchronized long createID() {
        return LONG_ADDER.incrementAndGet();
    }

}
