package com.tensor.api.org.util.alog;

/**
 * @Author: chuntaojun
 * @Date: 2019-03-26 22:16
 */
public class SimHashAlogUtil {


    public static String simHash(String str) {
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


}
