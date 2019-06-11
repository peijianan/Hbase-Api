package com.tensor.org.mq.serialize.gson;

import static com.tensor.org.mq.serialize.gson.ThreadLocalGsonFactory.gson;

/**
 * @author liaochuntao
 */
public class GsonSerializer {

    public static <T> T decode(String json, Class<T> type) {
        return gson().fromJson(json, type);
    }

    public static String encode(Object obj) {
        return gson().toJson(obj);
    }

}
