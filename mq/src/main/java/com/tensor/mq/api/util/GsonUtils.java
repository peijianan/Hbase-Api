package com.tensor.mq.api.util;

import com.google.gson.Gson;

/**
 * @author liaochuntao
 */
public class GsonUtils {

    private static ThreadLocal<Gson> GSON = ThreadLocal.withInitial(Gson::new);

    public static String toJson(Object obj) {
        return GSON.get().toJson(obj);
    }
}
