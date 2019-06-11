package com.tensor.nacos.api.util;

import java.util.concurrent.atomic.LongAdder;

/**
 * @author <a href="mailto:liaochunyhm@live.com">liaochuntao</a>
 * @since
 */
public class IDUtils {

    private static final LongAdder LONG_ADDER = new LongAdder();

    public static synchronized long createID() {
        LONG_ADDER.increment();
        return LONG_ADDER.longValue();
    }

}
