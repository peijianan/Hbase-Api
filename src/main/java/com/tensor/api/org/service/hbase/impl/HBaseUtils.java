package com.tensor.api.org.service.hbase.impl;


import java.io.IOException;
import java.util.*;
import com.google.gson.JsonObject;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;



public class HBaseUtils  {

    public static String TABLE_NAME ="News";
    public static String cf1 ;
    public static String cf2;
    public static String id="id";
    public static String cf1_newTitle= "newTitle";
    public static String cf1_newType = "newType";
    public static String cf1_author = "author";
    public static String cf1_text = "text";
    public static String cf_2_hash_code;





     //设置表名
    public static void setTableName(String tableName)throws Exception {
        TABLE_NAME = tableName;
    }

     //生成rowkey
    public String getGoodId() {

        Random rand = new Random();
        int rad =rand.nextInt(90) + 10;
        UUID uuid = UUID.randomUUID();
        return String.valueOf(rad) + String.valueOf(uuid);
    }

     //转换Json
    public JsonObject jsonObjectTool(Result result,JsonObject jsonObject1){

        jsonObject1.addProperty("id", String.valueOf(result.getValue(Bytes.toBytes(cf1), Bytes.toBytes(id))));
        jsonObject1.addProperty("author", String.valueOf(result.getValue(Bytes.toBytes(cf1), Bytes.toBytes(cf1_author))));
        jsonObject1.addProperty("newTitle", String.valueOf(result.getValue(Bytes.toBytes(cf1), Bytes.toBytes(cf1_newTitle))));
        jsonObject1.addProperty("newType", String.valueOf(result.getValue(Bytes.toBytes(cf1), Bytes.toBytes(cf1_newType))));
        jsonObject1.addProperty("text", String.valueOf(result.getValue(Bytes.toBytes(cf1), Bytes.toBytes(cf1_text))));

        return jsonObject1;
    }


}


