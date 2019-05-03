package com.tensor.api.org.util.alog;

import com.tensor.api.org.enpity.News;
import lombok.extern.slf4j.Slf4j;


/**
 * @Author: chuntaojun
 * @Date: 2019-03-26 22:16
 */
@Slf4j
public final class SimHashAlogUtils {

    private static final EnglishSimHashImpl ENGLISH_SIM_HASH = new EnglishSimHashImpl();
    private static final ChineseSimHashImpl CHINESE_SIM_HASH = new ChineseSimHashImpl();

    public static News nlp(News newsBean) {
        String str = newsBean.getText();
        String simHash;
        if (isEnglish(str)) {
            simHash = simHash(str, ENGLISH_SIM_HASH);
        } else {
            simHash = simHash(str, CHINESE_SIM_HASH);
        }
        log.info(simHash);
        newsBean.setHashCode(simHash);
        return newsBean;
    }

    private static String simHash(String str, LanguageSimHash languageSimHash) {
        return languageSimHash.simHash(str);
    }

    private static boolean isEnglish(String text) {
        return text.replaceAll("\\s+", "").matches("^[A-Za-z0-9%&',;=?]+$");
    }

    public static void main(String[] args) {
        String english = "A subpoena demanding the release of the full report into Russian meddling during the 2016 election has been issued, amid claims the current version leaves most of Congress in the dark";
        String chinese = "这篇文章主要讲如何使用正则匹配中文字符，中文正则表达式的匹配规则不像其他正则规则一样容易记住，下面一起看看这个中文正则表达式是怎么样的";
        nlp(News.builder().text(english).build());
    }

}
