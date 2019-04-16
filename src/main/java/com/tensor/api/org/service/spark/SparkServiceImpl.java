package com.tensor.api.org.service.spark;

import com.tensor.api.org.enpity.News;
import com.tensor.api.org.service.spark.base.BaseSparkServiceImpl;
import com.tensor.api.org.service.spark.similarity.SimilarityServiceImpl;
import com.tensor.api.org.service.spark.statistics.StatisticsService;
import com.tensor.api.org.service.spark.statistics.StatisticsServiceImpl;
import org.apache.spark.api.java.JavaPairRDD;
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

        JavaPairRDD<String , Integer> wordFrequency1 = statisticsService.getWordFrequency(news);

        System.out.println(wordFrequency1.count());

        SimilarityServiceImpl similarityService = new SimilarityServiceImpl();
        //此时已经排好序
        JavaPairRDD<Long , Integer>  similarNewsIdRDD = similarityService.getSimilarityList(wordFrequency1);
        List<News> newsList = new ArrayList<>();
        similarNewsIdRDD.foreach(new VoidFunction<Tuple2<Long, Integer>>() {
            @Override
            public void call(Tuple2<Long, Integer> longIntegerTuple2) throws Exception {
                //todo:通过tuple2._1找到对应的新闻记录并加入到List中
                newsList.add(News.builder().id(longIntegerTuple2._1).build());
            }
        });
        return newsList;
    }
}
