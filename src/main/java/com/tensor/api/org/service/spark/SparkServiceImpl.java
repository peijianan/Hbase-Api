package com.tensor.api.org.service.spark;

import com.tensor.api.org.enpity.News;
import com.tensor.api.org.service.spark.base.BaseSparkServiceImpl;
import com.tensor.api.org.service.spark.similarity.SimilarityService;
import com.tensor.api.org.service.spark.similarity.SimilarityServiceImpl;
import com.tensor.api.org.service.spark.statistics.StatisticsService;
import com.tensor.api.org.service.spark.statistics.StatisticsServiceImpl;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;

/**
 * spark对外接口实现类
 *
 * @author LightDance
 * @date 2019/4/3
 */
public class SparkServiceImpl implements SparkService{
    @Override
    public <T> void distinctNews(T news) {
        //。。。
    }

    @Override
    public List<News> getSimilarity(News news) {
        BaseSparkServiceImpl.init();
        StatisticsService statisticsService = new StatisticsServiceImpl();

        //获取其中最长的若干句
        JavaPairRDD<String , Integer> longestSentences = statisticsService.getLongestSentences(news)
                .mapToPair((PairFunction<String, String, Integer>) s -> new Tuple2<>(s , 1));

        SimilarityServiceImpl similarityService = new SimilarityServiceImpl();
        //此时已经排好序
        JavaPairRDD<Long , Integer>  similarNewsIdRDD = similarityService.getSimilarityList(longestSentences);
        List<News> newsList = new ArrayList<>();
        similarNewsIdRDD.foreach((VoidFunction<Tuple2<Long, Integer>>) longIntegerTuple2 -> {
            //todo:通过tuple2._1找到对应的新闻记录并加入到List中
            newsList.add(News.builder().id(longIntegerTuple2._1).build());
        });
        return newsList;
    }

    @Override
    public int getSimilarityOfTwo(News news1, News news2) {
        BaseSparkServiceImpl.init();
        StatisticsService statisticsService = new StatisticsServiceImpl();

        JavaPairRDD<String , Integer> sentenceFrequency1 = statisticsService.getLongestSentences(news1)
                .mapToPair((PairFunction<String, String, Integer>) s -> new Tuple2<>(s , 1));
        JavaPairRDD<String , Integer> sentenceFrequency2 = statisticsService.getLongestSentences(news2)
                .mapToPair((PairFunction<String, String, Integer>) s -> new Tuple2<>(s , 1));

        SimilarityService similarityService = new SimilarityServiceImpl();
        int hammingResult = similarityService.getSimilarityOfTwo(sentenceFrequency1 , sentenceFrequency2);
        System.out.println("海明距离： " + hammingResult);
        return hammingResult;
    }
}
