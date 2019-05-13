package com.tensor.nacos.api.util;

import com.tensor.nacos.api.pojo.SystemMonitor;

/**
 * @author liaochuntao
 */
public class LoadBlanceUtils {

    public static double load(SystemMonitor monitor) {
        return monitor.getAvailable() * 1.0 / 1024 / 1024 / 1000;
    }

}

