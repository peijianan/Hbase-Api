package com.tensor.api.org.util.alog;

import com.tensor.api.org.enpity.News;
import lombok.extern.slf4j.Slf4j;
import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: chuntaojun
 * @Date: 2019-03-26 22:16
 */
@Slf4j
public final class SimHashAlogUtil {

    private final static int TOP_K_KEY_WORD = 50;

    public static News simHash(News newsBean) {
        String str = newsBean.getText();
        KeyWordComputer kwc = new KeyWordComputer(TOP_K_KEY_WORD);
        Collection<Keyword> keyList = kwc.computeArticleTfidf(str);
        Map<String, Double> keyWords = keyList.stream()
                .collect(HashMap::new, (m, v) -> m.put(v.getName(), v.getScore()), HashMap::putAll);
        ArrayList<ArrayList<Integer>> keyScores = new ArrayList<>();
        keyWords.entrySet().stream().peek(entry -> {
            int weight = (int) (entry.getValue() * 20);
            ArrayList<Integer> tmp = new ArrayList<>();
            String feature = hash(entry.getKey());
            for (int i = 0; i < feature.length(); i ++) {
                if (feature.charAt(i) == '1') {
                    tmp.add(weight);
                } else {
                    tmp.add(-1 * weight);
                }
            }
            keyScores.add(tmp);
        }).count();
            ArrayList<Integer> answer = sum(keyScores);
        if (answer.isEmpty()) {
            newsBean.setHashCode("00");
        }
        StringBuilder simHash = new StringBuilder();
        answer.stream().peek(integer -> {
            if (integer > 0) {
                simHash.append('1');
            } else {
                simHash.append('0');
            }
        }).count();
        log.info(simHash.toString());
        newsBean.setHashCode(simHash.toString());
        return newsBean;
    }

    private static String hash(String str) {
        if ("".equals(str)) {
            return "0";
        }
        else {
            char[] chars = str.toCharArray();
            long x = chars[0] << 7;
            long m = 1000003;
            long mask = (long) (Math.pow(2, 128) - 1);
            for (char c : chars) {
                x = ((x * m) ^ c) & mask;
            }
            x ^= chars.length;
            if (x == -1) {
                x = -2;
            }
            return zeroFill(Long.toBinaryString(x).replace("0b", ""));
        }
    }

    private static ArrayList<Integer> sum(ArrayList<ArrayList<Integer>> tmp) {
        return (ArrayList<Integer>) tmp
                .stream()
                .map(list -> list.stream()
                        .reduce((x, y) -> x + y).get())
                .collect(Collectors.toList());
    }

    private static String zeroFill(String s) {
        StringBuilder zero = new StringBuilder();
        int a = 64 - s.length();
        for (int i = 0; i < a; i ++) {
            zero.append('0');
        }
        zero.append(s);
        return zero.toString();
    }

}
