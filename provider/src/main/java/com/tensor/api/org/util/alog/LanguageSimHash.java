package com.tensor.api.org.util.alog;

/**
 * @author liaochuntao
 */
public interface LanguageSimHash {

    int HASH_BITS = 64;

    /**
     *
     * @param str
     * @return
     */
    String simHash(String str);

}
