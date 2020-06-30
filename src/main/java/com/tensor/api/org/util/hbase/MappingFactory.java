package com.tensor.api.org.util.hbase;

import com.google.gson.Gson;
import com.tensor.api.org.annotation.hbase.Table;
import com.tensor.api.org.enpity.ColumnData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.data.annotation.Id;
import org.springframework.util.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liaochuntao
 */
@Slf4j
public class MappingFactory {

    public static final MappingFactory MAPPING_FACTORY;

    private static final Set<Class> BASIC_TYPE = new HashSet<>();

    static {
        MAPPING_FACTORY = new MappingFactory();
        BASIC_TYPE.add(Short.class);
        BASIC_TYPE.add(Integer.class);
        BASIC_TYPE.add(Long.class);
        BASIC_TYPE.add(String.class);
        BASIC_TYPE.add(Character.class);
        BASIC_TYPE.add(Date.class);
    }

    private static ConcurrentHashMap<Class, Mapper> MAPPER_CACHE = new ConcurrentHashMap<>();

    public Mapper register(Class cls) {
        return MAPPER_CACHE.put(cls, new Mapper(cls));
    }

    private Mapper getMapper(Class cls) {
        return MAPPER_CACHE.get(cls);
    }

    public <T> T mapToObj(Result result, Class<T> cls) {
        Mapper mapper = getMapper(cls);
        if (Objects.isNull(mapper)) {
            register(cls);
            mapper = getMapper(cls);
        }
        T t = null;
        try {
            t = cls.newInstance();
            for (Map.Entry<String, ColumnInfo> entry : mapper.getColumnInfoMap().entrySet()) {
                ColumnInfo columnInfo = entry.getValue();
                Field field = columnInfo.getField();
                Handler handler = columnInfo.getHandler();
                field.setAccessible(true);
                Object value = handler.adaper(result.getValue(Bytes.toBytes(columnInfo.getCluster()), Bytes.toBytes(columnInfo.getName())));
                field.set(t, value);
            }
        } catch (InstantiationException | IllegalAccessException
                | InvocationTargetException e) {
            log.error("[MappingFactory] : {}", e);
        }
        return t;
    }

    public List<ColumnData> buildColumnData(Object saveData) throws IllegalAccessException {
        Class<?> cls = saveData.getClass();
        Mapper mapper = getMapper(cls);
        if (Objects.isNull(mapper)) {
            register(cls);
            mapper = getMapper(cls);
        }

        List<ColumnData> columnDatas = new LinkedList<>();

        Table table = cls.getAnnotation(Table.class);
        String tableName = table.name();
        String rowKey = null;

        for (Field field : mapper.fields) {
            if (field.isAnnotationPresent(Id.class)) {
                rowKey = String.valueOf(field.get(saveData));
                break;
            }
        }

        if (StringUtils.isEmpty(rowKey)) {
            throw new IllegalArgumentException("RowKey can't not be null");
        }

        for (Map.Entry<String, ColumnInfo> entry : mapper.getColumnInfoMap().entrySet()) {
            ColumnInfo columnInfo = entry.getValue();
            Field field = columnInfo.getField();
            field.setAccessible(true);
            ColumnData data = new ColumnData();
            data.setTableName(tableName);
            data.setColumn(columnInfo.name);
            data.setCf(columnInfo.cluster);
            data.setRowKey(rowKey);
            data.setValue(String.valueOf(field.get(saveData)));

            columnDatas.add(data);
        }
        return columnDatas;
    }

    @Data
    private static class Mapper {

        private String tableName;
        private Field[] fields;
        private Constructor[] constructors;

        private Map<String, ColumnInfo> columnInfoMap = new HashMap<>();

        Mapper(Class cls) {
            init(cls);
        }

        void init(Class cls) {
            if (cls.isAnnotationPresent(Table.class)) {
                Table table = (Table) cls.getAnnotation(Table.class);
                tableName = table.name();
            } else {
                tableName = cls.getName();
            }
            fields = cls.getDeclaredFields();
            constructors = cls.getConstructors();
            for (Field field : fields) {
                if (field.isAnnotationPresent(com.tensor.api.org.annotation.hbase.Field.class)) {
                    com.tensor.api.org.annotation.hbase.Field f = field.getAnnotation(com.tensor.api.org.annotation.hbase.Field.class);
                    ColumnInfo columnInfo = ColumnInfo.builder()
                            .name(f.name())
                            .cluster(f.cluster())
                            .field(field)
                            .handler(new Handler(field.getType()))
                            .build();
                    columnInfoMap.put(field.getName(), columnInfo);
                }
            }
        }

    }

    @Data
    @Builder
    @AllArgsConstructor
    private static final class ColumnInfo<T> {
        private String name;
        private String cluster;
        private Field field;
        private Handler<T> handler;
    }

    private static class Handler<T> {

        private Gson gson = new Gson();
        private Class<T> type;
        private Constructor constructor;

        Handler(Class<T> type) {
            this.type = type;
            try {
                this.constructor = type.getConstructor(String.class);
            } catch (NoSuchMethodException e) {
                log.error("[MappingFactory.Handler ERROR] : {}", e.getMessage());
            }
        }

        private <T> T adaper(byte[] value) throws IllegalAccessException, InstantiationException, InvocationTargetException {
            if (Objects.isNull(value)) {
                return null;
            }
            return adaper(new String(value));
        }

        private <T> T adaper(String value) throws IllegalAccessException, InvocationTargetException, InstantiationException {
            if (Objects.isNull(value)) {
                return null;
            }
            if (BASIC_TYPE.contains(type)) {
                return (T) constructor.newInstance(value);
            } else {
                return (T) gson.fromJson(value, type);
            }
        }
    }

}
