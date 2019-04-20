package com.tensor.api.org.service.hbase.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tensor.api.org.enpity.News;
import com.tensor.api.org.enpity.ResultData;
import com.tensor.api.org.service.hbase.HBaseBasicService;
import com.tensor.api.org.service.hbase.HBaseNewsService;
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

    @Override
    public Mono<ResultData<Boolean>> putNews(News news) {
        ResultData resultData = new ResultData();
        HBaseUtils hbBaseUtils = new HBaseUtils();
        String id = hbBaseUtils.getGoodId();
        boolean flag;
        try {
            boolean a = hBaseBasicService.putdata(hbBaseUtils.TABLE_NAME, id, hbBaseUtils.cf1, hbBaseUtils.cf1_author, news.getAuthor());
            boolean b = hBaseBasicService.putdata(hbBaseUtils.TABLE_NAME, id, hbBaseUtils.cf1, hbBaseUtils.cf1_newType, news.getNewType());
            boolean c = hBaseBasicService.putdata(hbBaseUtils.TABLE_NAME, id, hbBaseUtils.cf1, hbBaseUtils.cf1_newTitle, news.getNewTitle());
            boolean d = hBaseBasicService.putdata(hbBaseUtils.TABLE_NAME, id, hbBaseUtils.cf1, hbBaseUtils.cf1_text, news.getText());
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
    public Mono<ResultData<JsonObject>> getAllNews() {
        ResultData resultData = new ResultData();
        JsonArray array = new JsonArray();
        JsonObject object = new JsonObject();
        try {
            HBaseUtils hbBaseUtils = new HBaseUtils();
            ResultScanner res = hBaseBasicService.scantable(hbBaseUtils.TABLE_NAME);
            for (Result ress : res) {
                JsonObject jsonObject = new JsonObject();
                jsonObject = hbBaseUtils.jsonObjectTool(ress, jsonObject);
                array.add(jsonObject);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        object.add("data", array);
        resultData.setData(object);
        return Mono.justOrEmpty(resultData);
    }

    @Override
    public Mono<ResultData<JsonObject>> getAllAuthor() {
        Mono<ResultData<JsonObject>> dataMono = Mono.empty();
        ResultData resultData = new ResultData();
        JsonArray array = new JsonArray();
        JsonObject object = new JsonObject();
        try {
            HBaseUtils hbBaseUtils = new HBaseUtils();
            ResultScanner res = hBaseBasicService.QualifierFilter(hbBaseUtils.TABLE_NAME, hbBaseUtils.cf1_author);
            for (Result ress : res) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("id", String.valueOf(ress.getValue(Bytes.toBytes(hbBaseUtils.cf1), Bytes.toBytes(hbBaseUtils.id))));
                jsonObject.addProperty("author", String.valueOf(ress.getValue(Bytes.toBytes(hbBaseUtils.cf1), Bytes.toBytes(hbBaseUtils.cf1_author))));
                array.add(jsonObject);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        object.add("data", array);
        resultData.setData(object);
        return Mono.justOrEmpty(resultData);
    }

    @Override
    public Mono<ResultData<JsonObject>> getAllTitle() {
        Mono<ResultData<JsonObject>> dataMono = Mono.empty();
        ResultData resultData = new ResultData();
        JsonArray array = new JsonArray();
        JsonObject object = new JsonObject();
        try {
            HBaseUtils hbBaseUtils = new HBaseUtils();
            ResultScanner res = hBaseBasicService.QualifierFilter(hbBaseUtils.TABLE_NAME, hbBaseUtils.cf1_newTitle);
            for (Result ress : res) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("id", String.valueOf(ress.getValue(Bytes.toBytes(hbBaseUtils.cf1), Bytes.toBytes(hbBaseUtils.id))));
                jsonObject.addProperty("newTitle", String.valueOf(ress.getValue(Bytes.toBytes(hbBaseUtils.cf1), Bytes.toBytes(hbBaseUtils.cf1_newTitle))));
                array.add(jsonObject);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        object.add("data", array);
        resultData.setData(resultData);
        return Mono.justOrEmpty(resultData);
    }

    @Override
    public Mono<ResultData<JsonObject>> getNewsByRowKey(String rowKey) {
        Mono<ResultData<JsonObject>> dataMono = Mono.empty();
        ResultData resultData = new ResultData();
        JsonArray array = new JsonArray();
        JsonObject object = new JsonObject();
        try {
            HBaseUtils hbBaseUtils = new HBaseUtils();
            hBaseBasicService.RowFilter(hbBaseUtils.TABLE_NAME, rowKey);
            ResultScanner res = hBaseBasicService.WhileMatchbycolumnFilter(hbBaseUtils.TABLE_NAME, rowKey);
            for (Result ress : res) {
                JsonObject jsonObject = new JsonObject();
                jsonObject = hbBaseUtils.jsonObjectTool(ress, jsonObject);
                array.add(jsonObject);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        object.add("data", array);
        resultData.setData(object);
        return Mono.justOrEmpty(resultData);
    }

    @Override
    public Mono<ResultData<JsonObject>> getNewsByTitle(String newTitle) {
        Mono<ResultData<JsonObject>> dataMono = Mono.empty();
        ResultData resultData = new ResultData();
        JsonArray array = new JsonArray();
        JsonObject object = new JsonObject();
        try {
            HBaseUtils hbBaseUtils = new HBaseUtils();
            hBaseBasicService.RowFilter(hbBaseUtils.TABLE_NAME, newTitle);
            ResultScanner res = hBaseBasicService.WhileMatchbycolumnFilter(hbBaseUtils.TABLE_NAME, newTitle);
            for (Result ress : res) {
                JsonObject jsonObject = new JsonObject();
                jsonObject = hbBaseUtils.jsonObjectTool(ress, jsonObject);
                array.add(jsonObject);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        object.add("data", array);
        resultData.setData(object);
        return Mono.justOrEmpty(resultData);

    }

    @Override
    public Mono<ResultData<JsonObject>> getNewsByType(String newType) {
        Mono<ResultData<JsonObject>> dataMono = Mono.empty();
        ResultData resultData = new ResultData();
        JsonArray array = new JsonArray();
        JsonObject object = new JsonObject();
        try {
            HBaseUtils hbBaseUtils = new HBaseUtils();
            hBaseBasicService.RowFilter(hbBaseUtils.TABLE_NAME, newType);
            ResultScanner res = hBaseBasicService.WhileMatchbycolumnFilter(hbBaseUtils.TABLE_NAME, newType);
            for (Result ress : res) {
                JsonObject jsonObject = new JsonObject();
                jsonObject = hbBaseUtils.jsonObjectTool(ress, jsonObject);
                array.add(jsonObject);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        object.add("data", array);
        resultData.setData(object);
        return Mono.justOrEmpty(resultData);
    }

    @Override
    public Mono<ResultData<JsonObject>> getNewsByAuthor(String author) {
        Mono<ResultData<JsonObject>> dataMono = Mono.empty();
        ResultData resultData = new ResultData();
        JsonArray array = new JsonArray();
        JsonObject object = new JsonObject();
        try {
            HBaseUtils hbBaseUtils = new HBaseUtils();
            hBaseBasicService.RowFilter(hbBaseUtils.TABLE_NAME, author);
            ResultScanner res = hBaseBasicService.WhileMatchbycolumnFilter(hbBaseUtils.TABLE_NAME, author);
            for (Result ress : res) {
                JsonObject jsonObject = new JsonObject();
                jsonObject = hbBaseUtils.jsonObjectTool(ress, jsonObject);
                array.add(jsonObject);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        object.add("data", array);
        resultData.setData(object);
        return Mono.justOrEmpty(resultData);


    }


}
