/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tensor.api.org.db.operation;

import java.util.List;

/**
 * @author <a href="mailto:liaochunyhm@live.com">liaochuntao</a>
 * @since
 */
public interface Select<T> {

    /**
     * 根据对象的属性查找相应的对象
     *
     * @param query 查找条件对象，即你要查询什么对象，想根据什么查，直接将对应的属性设置为查找条件
     *              例如有一个User表，属性有{id,name,birthday}，现在你想查询{id=10}的User，则
     *              new User(id = 10)，调用该接口即可
     * @return 查找结果
     */
    T selectOne(T query);

    /**
     * 根据对象的属相查找符合条件的结果集
     *
     * @param query 查找条件对象
     * @return
     */
    List<T> selectList(T query);

    /**
     *
     * @param query
     * @param id
     * @param num
     * @return
     */
    List<T> selectListByPage(T query, int id, int num);

}
