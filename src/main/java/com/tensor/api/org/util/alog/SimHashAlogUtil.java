package com.tensor.api.org.util.alog;

import lombok.extern.slf4j.Slf4j;
import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: chuntaojun
 * @Date: 2019-03-26 22:16
 */
@Slf4j
public final class SimHashAlogUtil {

    private final static int TOP_K_KEY_WORD = 50;

    public static String simHash(String str) {
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
        return null;
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
            String ans = String.format("%064d", Long.toBinaryString(x).replace("0b", ""));
            return ans;
        }
    }

    public static void main(String[] args) {
        ArrayList<? extends SimHashAlogUtil> list = new ArrayList<>();
        ArrayList<? super SimHashAlogUtil> list1 = new ArrayList<>();
        System.out.println(list.toString() + list1.toString());
    }

}
