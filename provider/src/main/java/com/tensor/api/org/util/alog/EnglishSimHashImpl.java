package com.tensor.api.org.util.alog;

import java.math.BigInteger;
import java.util.StringTokenizer;

/**
 * @author liaochuntao
 */
public class EnglishSimHashImpl implements LanguageSimHash {

    @Override
    public String simHash(String str) {
        int[] v = new int[HASH_BITS];
        StringTokenizer stringTokens = new StringTokenizer(str);
        while (stringTokens.hasMoreTokens()) {
            String temp = stringTokens.nextToken();
            BigInteger t = hash(temp);
            for (int i = 0; i < HASH_BITS; i++) {
                BigInteger bitmask = new BigInteger("1").shiftLeft(i);
                if (t.and(bitmask).signum() != 0) {
                    v[i] += 1;
                } else {
                    v[i] -= 1;
                }
            }
        }
        BigInteger fingerprint = new BigInteger("0");
        StringBuilder simHashBuffer = new StringBuilder();
        for (int i = 0; i < HASH_BITS; i++) {
            if (v[i] >= 0) {
                fingerprint = fingerprint.add(new BigInteger("1").shiftLeft(i));
                simHashBuffer.append("1");
            }else{
                simHashBuffer.append("0");
            }
        }
        return simHashBuffer.toString();
    }

    private static BigInteger hash(String source) {
        if (source == null || source.length() == 0) {
            return new BigInteger("0");
        } else {
            char[] sourceArray = source.toCharArray();
            BigInteger x = BigInteger.valueOf(((long) sourceArray[0]) << 7);
            BigInteger m = new BigInteger("1000003");
            BigInteger mask = new BigInteger("2").pow(HASH_BITS).subtract(
                    new BigInteger("1"));
            for (char item : sourceArray) {
                BigInteger temp = BigInteger.valueOf((long) item);
                x = x.multiply(m).xor(temp).and(mask);
            }
            x = x.xor(new BigInteger(String.valueOf(source.length())));
            if (x.equals(new BigInteger("-1"))) {
                x = new BigInteger("-2");
            }
            return x;
        }
    }
}
