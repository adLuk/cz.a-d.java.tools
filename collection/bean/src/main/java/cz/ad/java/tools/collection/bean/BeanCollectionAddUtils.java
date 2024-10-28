/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cz.ad.java.tools.collection.bean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Extension of bean collection utils to simplify amount of parameters for adding by selecting type of collection or map
 * used for initialization of empty parameters by method name.
 */
public interface BeanCollectionAddUtils extends BeanCollectionUtils {

    /**
     * Add safely value into bean collection member accessed by provided getter, if input collection is null use init to
     * supply new instance of collection.
     *
     * @param getter supplier providing access to collection member in bean.
     * @param setter consumer used in case when bean member is null and needs to be initialized.
     * @param value  value to be added into list.
     * @param <V>    type of value.
     * @return provided list or initialized new instance of ArrayList with added value.
     */
    default <V> List<V> addList(Supplier<List<V>> getter, Consumer<List<V>> setter, V value) {
        return add(getter, setter, ArrayList::new, value);
    }

    /**
     * Add safely value into bean collection member accessed by provided getter, if input collection is null use init to
     * supply new instance of collection.
     *
     * @param getter supplier providing access to collection member in bean.
     * @param setter consumer used in case when bean member is null and needs to be initialized.
     * @param value  value to be added into collection.
     * @param <V>    type of value.
     * @return provided set or initialized new instance of HashSet with added value.
     */
    default <V> Set<V> addSet(Supplier<Set<V>> getter, Consumer<Set<V>> setter, V value) {
        return add(getter, setter, HashSet::new, value);
    }

    /**
     * Add safely values from source collection into bean collection member accessed by provided getter if provided
     * destination is null it is initialized by init supplier. If provided source is null or empty target without change
     * is return.
     *
     * @param getter supplier providing access to collection member in bean.
     * @param setter consumer used in case when bean member is null and needs to be initialized.
     * @param source values to be added into collection.
     * @param <S>    type of source collection.
     * @param <V>    type of value in collection.
     * @return original target list or initialized new ArrayList with added provided values if source was not empty,
     *         otherwise target collection parameter value without change.
     */
    default <S extends Collection<V>, V> List<V> addAllList(
            Supplier<List<V>> getter, Consumer<List<V>> setter, S source
    ) {
        return addAll(getter, setter, ArrayList::new, source);
    }

    /**
     * Add safely values from source collection into bean collection member accessed by provided getter if provided
     * destination is null it is initialized by init supplier. If provided source is null or empty target without change
     * is return.
     *
     * @param getter supplier providing access to collection member in bean.
     * @param setter consumer used in case when bean member is null and needs to be initialized.
     * @param source values to be added into collection.
     * @param <S>    type of source collection.
     * @param <V>    type of value in collection.
     * @return original target list or initialized new ArrayList with added provided values if source was not empty,
     *         otherwise target collection parameter value without change.
     */
    default <S extends Collection<V>, V> Set<V> addAllSet(Supplier<Set<V>> getter, Consumer<Set<V>> setter, S source) {
        return addAll(getter, setter, HashSet::new, source);
    }

    /**
     * Put safely key and value pair into bean map member accessed by provided getter, if input map is null init is used
     * to supply new instance.
     *
     * @param getter supplier providing access to map member in bean.
     * @param setter consumer used in case when bean member is null and needs to be initialized.
     * @param key    key connected to value in map.
     * @param value  value stored in map connected to key.
     * @param <K>    type of key.
     * @param <V>    type of value.
     * @return provided map or initialized by supplier with added key and value.
     */
    default <K, V> Map<K, V> putMap(Supplier<Map<K, V>> getter, Consumer<Map<K, V>> setter, K key, V value) {
        return put(getter, setter, HashMap::new, key, value);
    }

    /**
     * Put safely values from source map into bean map member accessed by provided getter and when target map is null
     * new instance is created by provided supplier. When source map has no records target parameter is returned without
     * change.
     *
     * @param getter supplier providing access to map member in bean.
     * @param setter consumer used in case when bean member is null and needs to be initialized.
     * @param source map with source data
     * @param <S>    type of source map.
     * @param <K>    type of key.
     * @param <V>    type of value.
     * @return target map or initialized new map with all values from provided values map if it was not empty.
     *         Otherwise, value of target parameter without change.
     */
    default <S extends Map<K, V>, K, V> Map<K, V> putAllMap(
            Supplier<Map<K, V>> getter, Consumer<Map<K, V>> setter, S source
    ) {
        return putAll(getter, setter, HashMap::new, source);
    }
}
