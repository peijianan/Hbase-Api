package com.tensor.api.org.service.spark.base;

import java.io.Serializable;
import java.util.Comparator;

/**
 * 排序时需要用到{@link Comparator}接口，但它并没有实现序列化，因此总是报错。
 * 在这里给它序列化一下。
 *
 * 没有实现任何方法，仅作为mark interface定义类型
 *
 * todo: 序列化会导致安全性、内部结构难以处理，但目前这些还暂未解决
 * @author hadoop
 */
public interface SerializableComparator<T> extends Comparator<T>, Serializable {
}
