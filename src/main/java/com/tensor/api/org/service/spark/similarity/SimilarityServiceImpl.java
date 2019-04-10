package com.tensor.api.org.service.spark.similarity;

import com.tensor.api.org.enpity.News;
import com.tensor.api.org.service.spark.base.BaseSparkServiceImpl;
import com.tensor.api.org.service.spark.statistics.StatisticServiceImpl;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.spark_project.guava.primitives.Ints;
import org.springframework.data.annotation.Transient;
import scala.Tuple2;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.*;

/**
 * 相似度服务实现类
 *
 * @author LightDance
 */
public class SimilarityServiceImpl extends BaseSparkServiceImpl implements SimilarityService, Serializable {

    private static final int DEFAULT_HASH_BITS = 64;

    @Override
    public int getSimilarityOfTwo(JavaPairRDD<String, Integer> article1, JavaPairRDD<String, Integer> article2) {
        BigInteger simHashResult1 = getSimHashValue(article1);
        BigInteger simHashResult2 = getSimHashValue(article2);

        return hammingDistance(simHashResult1, simHashResult2);
    }

    @Override
    public JavaPairRDD<Long, Integer> getSimilarityList(JavaPairRDD<String, Integer> article) {
        BigInteger simHashResult1 = getSimHashValue(article);
        JavaRDD<News> allArticles = sc.parallelize(getAllArticles());

        JavaPairRDD<Long, Integer> result = allArticles
                //映射成newsId-newsContent形式
                .mapToPair(new PairFunction<News, Long, JavaPairRDD<String, Integer>>() {
                    @Override
                    public Tuple2<Long, JavaPairRDD<String, Integer>> call(News news) throws Exception {
                        return new Tuple2<>(news.getId(), new StatisticServiceImpl().getWordFrequency(news));
                    }
                })
                //计算simHash值,结果为newsId-newsSimHashValue形式
                .mapValues(new Function<JavaPairRDD<String, Integer>, BigInteger>() {
                    @Override
                    public BigInteger call(JavaPairRDD<String, Integer> v1) throws Exception {
                        return getSimHashValue(v1);
                    }
                })
                // 计算海明距离，结果为newsId-hammingDistance形式
                .mapValues(new Function<BigInteger, Integer>() {
                    @Override
                    public Integer call(BigInteger v1) throws Exception {
                        return hammingDistance(v1, simHashResult1);
                    }
                })
                //键值位置互换，形式变成hammingDistance-newsId形式
                .mapToPair(new PairFunction<Tuple2<Long, Integer>, Integer, Long>() {
                    @Override
                    public Tuple2<Integer, Long> call(Tuple2<Long, Integer> longIntegerTuple2) throws Exception {
                        return new Tuple2<>(longIntegerTuple2._2, longIntegerTuple2._1);
                    }
                })
                //按照海明距离排序
                .sortByKey(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer v1, Integer v2) {
                        if (v1.equals(v2)) {
                            return 0;
                        } else if (v1 > v2) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }
                })
                //再把键值对顺序换回来
                .mapToPair(new PairFunction<Tuple2<Integer, Long>, Long, Integer>() {
                    @Override
                    public Tuple2<Long, Integer> call(Tuple2<Integer, Long> integerLongTuple2) throws Exception {
                        return  new Tuple2<>(integerLongTuple2._2, integerLongTuple2._1);
                    }
                });

        //debug
        System.out.println("ok");
        return result;

    }

    private BigInteger getSimHashValue(JavaPairRDD<String, Integer> wordFrequency) {

        List<Integer> valueList = wordFrequency.map((Function<Tuple2<String, Integer>, List<Integer>>) stringIntegerTuple2 -> {
            BigInteger t = getSimpleHashValue(stringIntegerTuple2._1);
            BigInteger bitmask = new BigInteger("1");
            Integer[] v = new Integer[DEFAULT_HASH_BITS];
            for (int i = 0; i < v.length; i++) {
                v[i] = 0;
            }
            // 计算v[i]  查看hash结果每一二进制位是否为0，之后对权重v[i]作相应加减操作
            for (int i = 0; i < v.length; i++) {
                if (t.and(bitmask).signum() != 0) {
                    v[i] += stringIntegerTuple2._2;
                } else {
                    v[i] -= stringIntegerTuple2._2;
                }
                bitmask = bitmask.shiftLeft(1);
            }
            return Arrays.asList(v);
        })
                //得到若干List, 每个list中实际上是权值数组
                .reduce((Function2<List<Integer>, List<Integer>, List<Integer>>) (v1, v2) -> {

                    if (v1.size() != v2.size()) {
                        throw new IllegalArgumentException("数组长度不一致");
                    }
                    //转回数组进行计算汇总
                    Integer[] arr1 = new Integer[v1.size()];
                    v1.toArray(arr1);
                    Integer[] arr2 = new Integer[v2.size()];
                    v2.toArray(arr2);
                    for (int i = 0; i < arr1.length; i++) {
                        arr1[i] += arr2[i];
                    }
                    return Arrays.asList(arr1);
                });
        //于是有了汇总后的权值List

        int[] valueArray = Ints.toArray(valueList);
        //终于得到hash结果
        return calculateFingerPrint(valueArray);
    }

    private BigInteger calculateFingerPrint(int[] values) {
        BigInteger fingerprint = new BigInteger("0");
        for (int i = 0; i < values.length; i++) {
            if (values[i] >= 0) {
                fingerprint = fingerprint.add(new BigInteger("1").shiftLeft(i));
            }
        }
        return fingerprint;
    }

    private BigInteger getSimpleHashValue(String str) {
        if (str == null || str.length() == 0) {
            return new BigInteger("0");
        } else {
            char[] sourceArray = str.toCharArray();
            BigInteger x = BigInteger.valueOf(((long) sourceArray[0]) << 7);
            BigInteger m = new BigInteger("1000003");
            BigInteger mask = new BigInteger("2").pow(DEFAULT_HASH_BITS).subtract(
                    new BigInteger("1"));
            for (char item : sourceArray) {
                BigInteger temp = BigInteger.valueOf((long) item);
                x = x.multiply(m).xor(temp).and(mask);
            }
            x = x.xor(new BigInteger(String.valueOf(str.length())));
            if (x.equals(new BigInteger("-1"))) {
                x = new BigInteger("-2");
            }
            return x;
        }
    }


    /**
     * 两个hash结果的海明距离
     *
     * @param simHashValue1 simHash值1
     * @param simHashValue2 simHash值2
     * @return 两者差异位的个数
     */
    @Transient
    private int hammingDistance(BigInteger simHashValue1, BigInteger simHashValue2) {

        BigInteger x = simHashValue1.xor(simHashValue2);
        int tot = 0;

        //统计x中二进制位数为1的个数
        //我们想想，一个二进制数减去1，那么，从数串最右那个"1"位置（包括那个1）往右的数字全都反了，
        //然后 n&(n-1)就相当于把后面的数字清0，
        //我们看n能做多少次这样的操作就OK了。

        while (x.signum() != 0) {
            tot += 1;
            x = x.and(x.subtract(new BigInteger("1")));
        }
        return tot;
    }

    /**
     * TODO 获取新闻列表的方法
     * @return List类型新闻列表
     */
    private List<News> getAllArticles() {
        return new ArrayList<>();
    }
}
