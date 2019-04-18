package com.tensor.api.org.service.spark.statistics;

import com.tensor.api.org.enpity.News;
import com.tensor.api.org.service.spark.base.BaseSparkServiceImpl;
import com.tensor.api.org.service.spark.base.SerializableComparator;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.*;
import org.apache.spark.rdd.RDD;
import org.springframework.data.annotation.Transient;
import scala.Function1;
import scala.Tuple2;
import scala.runtime.BoxedUnit;

import java.io.Serializable;
import java.util.*;

/**
 * 统计功能实现类
 *
 * @author LightDance
 * @date 2019/4/3
 */
public class StatisticsServiceImpl extends BaseSparkServiceImpl implements StatisticsService, Serializable {
    private static final String DEFAULT_SENTENCE_SPLIT_PATTERN = "([.\n]+\\s?)";
    private static final String DEFAULT_WORD_SPLIT_PATTERN = "[ \\pP\n\\s]+";
    private static final Integer DEFAULT_SENTENCES_NUM = 5;

    /**
     * 词频统计
     * TODO 未使用分词工具，目前仅统计单词
     *
     * @param article 文章
     * @return 文章中word列表
     */
    @Override
    public JavaPairRDD<String, Integer> getWordFrequency(News article) {
        String content = article.getText();
        return getWordFrequency(content);
    }

    @Override
    public JavaPairRDD<String, Integer> getWordFrequency(String sentence) {
        JavaRDD<String> content = sc.parallelize(Collections.singletonList(sentence));
        return getFrequency(sentence, DEFAULT_WORD_SPLIT_PATTERN);
    }

    /**
     * 统计句子
     *
     * @param article 文章
     * @return 文章中句子--权值列表
     */
    @Override
    public JavaPairRDD<String, Integer> getSentenceFrequency(News article) {
        return getSentenceFrequency(article, DEFAULT_SENTENCE_SPLIT_PATTERN);
    }

    @Override
    public JavaPairRDD<String, Integer> getSentenceFrequency(News article, String splitPattern) {
        return getFrequency(article.getText(), splitPattern);
    }

    @Override
    public JavaRDD<String> getLongestSentences(News article) {
        JavaPairRDD<String, Integer> sentenceFrequency = getSentenceFrequency(article);
        JavaRDD<String> longestSentences = sc.parallelize(
                sentenceFrequency
                        //从句子列表中将"句子-出现次数"形式改为"句子长度-句子"形式
                        .mapToPair((PairFunction<Tuple2<String, Integer>, Integer, String>) stringIntegerTuple2 -> new Tuple2<>(stringIntegerTuple2._1.length(), stringIntegerTuple2._1))
                        //按照句子长度排序
                        .sortByKey((SerializableComparator<Integer>) (integer, t1) -> {
                            System.out.println("len1 = " + integer + " , " + "len2 = " + t1);
                            if (integer > t1) {
                                return 1;
                            } else if (integer.equals(t1)) {
                                return 0;
                            } else {
                                return -1;
                            }
                        }, false)
                        //取出句子内容
                        .values()
                        //只拿前n句
                        .top(DEFAULT_SENTENCES_NUM)
        );
        return longestSentences;
    }

    /**
     * 词频统计/句频统计的具体实现逻辑
     *
     * @param str          统计内容(文章/文章中的某个句子)
     * @param splitPattern 正则表达式形式的分隔符
     * @return 词频/句频列表
     */
    private JavaPairRDD<String, Integer> getFrequency(String str, String splitPattern) {
        JavaRDD<String> content = sc.parallelize(Collections.singletonList(str));
        JavaPairRDD<String, Integer> result = content
                .flatMap((FlatMapFunction<String, String>) s -> {
                    //以换行、句号分开
                    String[] words = s.split(splitPattern);
                    return Arrays.asList(words).iterator();
                })
                .mapToPair((PairFunction<String, String, Integer>) s -> new Tuple2<>(s, 1))
                .reduceByKey((Function2<Integer, Integer, Integer>) (v1, v2) -> v1 + v2);
//        showMe(result);
        return result;
    }

}
