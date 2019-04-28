package com.tensor.api.org.service.hbase.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tensor.api.org.enpity.News;
import com.tensor.api.org.enpity.ResultData;
import com.tensor.api.org.service.hbase.HBaseBasicService;
import com.tensor.api.org.service.hbase.HBaseNewsService;
import com.tensor.api.org.service.mq.ProducerService;
import com.tensor.api.org.util.HBaseUtils;
import com.tensor.api.org.util.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service(value = "HBaseNewsService")
/**
 * @author peijianan
 */

public class HBaseNewsServiceImpl implements HBaseNewsService {

    @Autowired
    private HBaseBasicService hBaseBasicService;

    @Autowired
    private ProducerService producerService;

    @Override
    public Mono<ResultData<Boolean>> putNews(News news) {   //根据传入的参数将新闻插入数据库 返回是否成功
        ResultData resultData = new ResultData();
        boolean flag;
        try {
            String id = HBaseUtils.getGoodId();
            boolean a = hBaseBasicService.putdata(HBaseUtils.TABLE_NAME, id, HBaseUtils.cf1, HBaseUtils.cf1_author, news.getAuthor());
            boolean b = hBaseBasicService.putdata(HBaseUtils.TABLE_NAME, id, HBaseUtils.cf1, HBaseUtils.cf1_newType, news.getNewType());
            boolean c = hBaseBasicService.putdata(HBaseUtils.TABLE_NAME, id, HBaseUtils.cf1, HBaseUtils.cf1_newTitle, news.getNewTitle());
            boolean d = hBaseBasicService.putdata(HBaseUtils.TABLE_NAME, id, HBaseUtils.cf1, HBaseUtils.cf1_text, news.getText());
            flag = a && b && c && d;
            resultData.setData(flag);
            if (flag) {
                resultData.setCode(HttpStatus.OK.value());
            } else {
                resultData.buildFromResultCode(ResultCode.STORAGE_FAILURE);
            }
        } catch (Exception e) {
            resultData.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            resultData.setMsg(e.getLocalizedMessage());
            resultData.setData(false);
        }
        return Mono.justOrEmpty(resultData);

    }


    @Override
    public Mono<ResultData<JsonArray>> getAllNews() {  //读取全部新闻
        ResultData resultData = new ResultData();
        try {
            JsonArray array = new JsonArray();
            ResultScanner res = hBaseBasicService.scantable(HBaseUtils.TABLE_NAME);
            for (Result ress : res) {

                JsonObject jsonObject = new JsonObject();
                jsonObject = HBaseUtils.jsonObjectTool(ress, jsonObject);
                array.add(jsonObject);
            }
            resultData.setData(array.toString());
            resultData.setCode(HttpStatus.OK.value());
            resultData.setMsg("Ok!!!");
        } catch (Exception e) {
            resultData.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            resultData.setMsg(e.getLocalizedMessage());
            resultData.setData(false);
        }

        return Mono.justOrEmpty(resultData);
    }

    @Override
    public Mono<ResultData<JsonObject>> getAllAuthor() {    //读取全部作者 返回 行键-作者
        ResultData resultData = new ResultData();

        try {
            JsonArray array = new JsonArray();
            ResultScanner res = hBaseBasicService.QualifierFilter(HBaseUtils.TABLE_NAME, HBaseUtils.cf1_author);
            for (Result ress : res) {

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("id",Bytes.toString(ress.getRow()));
                jsonObject.addProperty("author", Bytes.toString(ress.getValue(Bytes.toBytes(HBaseUtils.cf1), Bytes.toBytes(HBaseUtils.cf1_author))));
                array.add(jsonObject);

            }
            resultData.setData(array);
            resultData.setCode(HttpStatus.OK.value());
            resultData.setMsg("OK!");
        } catch (Exception e) {
            resultData.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            resultData.setMsg(e.getLocalizedMessage());
            resultData.setData(false);
        }
        System.out.println(resultData);
        return Mono.justOrEmpty(resultData);
    }

    @Override
    public Mono<ResultData<JsonObject>> getAllTitle() {     //读取全部标题 返回 行键-标题
        ResultData resultData = new ResultData();
        try {

            JsonArray array = new JsonArray();
            ResultScanner res = hBaseBasicService.QualifierFilter(HBaseUtils.TABLE_NAME, HBaseUtils.cf1_newTitle);
            for (Result ress : res) {

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("id", Bytes.toString(ress.getRow()));
                jsonObject.addProperty("newTitle", Bytes.toString(ress.getValue(Bytes.toBytes(HBaseUtils.cf1), Bytes.toBytes(HBaseUtils.cf1_newTitle))));
                array.add(jsonObject);
            }
            resultData.setData(array);
            resultData.setCode(HttpStatus.OK.value());
            resultData.setMsg("OK!");
        } catch (Exception e) {
            resultData.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            resultData.setMsg(e.getLocalizedMessage());
            resultData.setData(false);
        }
        System.out.println(resultData);
        return Mono.justOrEmpty(resultData);
    }

    @Override
    public Mono<ResultData<JsonObject>> getNewsByRowKey(String rowKey) {    //根据行键读取新闻 返回对应新闻
        ResultData resultData = new ResultData();

        try {
            JsonArray array = new JsonArray();
            hBaseBasicService.RowFilter(HBaseUtils.TABLE_NAME, rowKey);
            ResultScanner res = hBaseBasicService.WhileMatchbycolumnFilter(HBaseUtils.TABLE_NAME, rowKey);
            for (Result ress : res) {

                JsonObject jsonObject = new JsonObject();
                jsonObject = HBaseUtils.jsonObjectTool(ress, jsonObject);
                array.add(jsonObject);

            }
            resultData.setData(array);
            resultData.setCode(HttpStatus.OK.value());
            resultData.setMsg("OK!");
        } catch (Exception e) {
            resultData.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            resultData.setMsg(e.getLocalizedMessage());
            resultData.setData(false);
        }
        System.out.println(resultData);
        return Mono.justOrEmpty(resultData);
    }

    @Override
    public Mono<ResultData<JsonObject>> getNewsByTitle(String newTitle) {   //根据标题读取新闻  返回对应新闻
        ResultData resultData = new ResultData();
        try {
            JsonArray array = new JsonArray();
            hBaseBasicService.RowFilter(HBaseUtils.TABLE_NAME, newTitle);
            ResultScanner res = hBaseBasicService.WhileMatchbycolumnFilter(HBaseUtils.TABLE_NAME, newTitle);
            for (Result ress : res) {

                JsonObject jsonObject = new JsonObject();
                jsonObject = HBaseUtils.jsonObjectTool(ress, jsonObject);
                array.add(jsonObject);
            }

            resultData.setData(array);
            resultData.setCode(HttpStatus.OK.value());
            resultData.setMsg("OK!");
        } catch (Exception e) {
            resultData.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            resultData.setMsg(e.getLocalizedMessage());
            resultData.setData(false);
        }

        return Mono.justOrEmpty(resultData);

    }

    @Override
    public Mono<ResultData<JsonObject>> getNewsByType(String newType) {     //根据分类读新闻
        ResultData resultData = new ResultData();
        JsonArray array = new JsonArray();

        try {
            HBaseUtils hbBaseUtils = new HBaseUtils();
            hBaseBasicService.RowFilter(hbBaseUtils.TABLE_NAME, newType);
            ResultScanner res = hBaseBasicService.WhileMatchbycolumnFilter(hbBaseUtils.TABLE_NAME, newType);
            for (Result ress : res) {

                JsonObject jsonObject = new JsonObject();
                jsonObject = hbBaseUtils.jsonObjectTool(ress, jsonObject);
                array.add(jsonObject);
            }

            resultData.setData(array);
            resultData.setCode(HttpStatus.OK.value());
            resultData.setMsg("OK!");
        } catch (Exception e) {
            resultData.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            resultData.setMsg(e.getLocalizedMessage());
            resultData.setData(false);
        }

        return Mono.justOrEmpty(resultData);
    }

    @Override
    public Mono<ResultData<JsonObject>> getNewsByAuthor(String author) {    //根据作者读新闻    返回对应新闻
        ResultData resultData = new ResultData();
        JsonArray array = new JsonArray();

        try {
            HBaseUtils hbBaseUtils = new HBaseUtils();
            hBaseBasicService.RowFilter(hbBaseUtils.TABLE_NAME, author);
            ResultScanner res = hBaseBasicService.WhileMatchbycolumnFilter(hbBaseUtils.TABLE_NAME, author);
            for (Result ress : res) {

                JsonObject jsonObject = new JsonObject();
                jsonObject = hbBaseUtils.jsonObjectTool(ress, jsonObject);
                array.add(jsonObject);
            }
            resultData.setData(array);
            resultData.setCode(HttpStatus.OK.value());
            resultData.setMsg("OK!");

        } catch (Exception e) {
            resultData.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            resultData.setMsg(e.getLocalizedMessage());
            resultData.setData(false);
        }

        return Mono.justOrEmpty(resultData);


    }


}
