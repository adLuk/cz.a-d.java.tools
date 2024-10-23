package cz.ad.java.tools.collection.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Extension of collection utils to simplify amount of parameters for adding by selecting type of collection or map used
 * for initialization of empty parameters.
 */
public interface CollectionAddUtils extends CollectionUtils {
    /**
     * Add safely value to list, if input collection is null use init to supply new instance of collection.
     *
     * @param list  used as target for adding value.
     * @param value value to be added into list.
     * @param <V>   type of value.
     * @return provided list or initialized new instance of ArrayList with added value.
     */
    default <V> List<V> addList(List<V> list, V value) {
        return add(list, ArrayList::new, value);
    }

    /**
     * Add safely value to set, if input collection is null use init to supply new instance of collection.
     *
     * @param set   used as target for adding value.
     * @param value value to be added into collection.
     * @param <V>   type of value.
     * @return provided set or initialized new instance of HashSet with added value.
     */
    default <V> Set<V> addSet(Set<V> set, V value) {
        return add(set, HashSet::new, value);
    }

    /**
     * Add safely values from source collection into destination list if provided destination is null it is initialized
     * by init supplier. If provided source is null or empty target without change is return.
     *
     * @param list   used as target for adding value.
     * @param source values to be added into collection.
     * @param <S>    type of source collection.
     * @param <V>    type of value in collection.
     * @return original target list or initialized new ArrayList with added provided values if source was not empty,
     *         otherwise target collection parameter value without change.
     */
    default <S extends Collection<V>, V> List<V> addAllList(List<V> list, S source) {
        return addAll(list, ArrayList::new, source);
    }

    /**
     * Add safely values from source collection into destination list if provided destination is null it is initialized
     * by init supplier. If provided source is null or empty target without change is return.
     *
     * @param list   used as target for adding value.
     * @param source values to be added into collection.
     * @param <S>    type of source collection.
     * @param <V>    type of value in collection.
     * @return original target list or initialized new ArrayList with added provided values if source was not empty,
     *         otherwise target collection parameter value without change.
     */
    default <S extends Collection<V>, V> Set<V> addAllSet(Set<V> list, S source) {
        return addAll(list, HashSet::new, source);
    }

    /**
     * Put safely key and value pair into map, if input map is null init is used to supply new instance.
     *
     * @param map   used as target for adding values.
     * @param key   key connected to value in map.
     * @param value value stored in map connected to key.
     * @param <K>   type of key.
     * @param <V>   type of value.
     * @return provided map or initialized by supplier with added key and value.
     */
    default <K, V> Map<K, V> putMap(Map<K, V> map, K key, V value) {
        return put(map, HashMap::new, key, value);
    }

    /**
     * Put safely values from source map into target map and when target map is null new instance is created by provided
     * supplier. When source map has no records target parameter is returned without change.
     *
     * @param target map for records provided in values map.
     * @param source map with source data
     * @param <S>    type of source map.
     * @param <K>    type of key.
     * @param <V>    type of value.
     * @return target map or initialized new map with all values from provided values map if it was not empty.
     *         Otherwise, value of target parameter without change.
     */
    default <S extends Map<K, V>, K, V> Map<K, V> putAllMap(Map<K, V> target, S source) {
        return putAll(target, HashMap::new, source);
    }
}
