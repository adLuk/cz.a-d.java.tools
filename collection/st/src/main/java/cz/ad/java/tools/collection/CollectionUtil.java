package cz.ad.java.tools.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Collection utils providing save operations with null values. Support basic CRUD operations with all collections
 * including maps.
 */
public class CollectionUtil {

    /**
     * Verify if collection has no records.
     *
     * @param collection instance to be validated.
     * @param <C>        type of collection.
     * @param <V>        type of value in collection.
     * @return true if provided collection is null or empty, otherwise false.
     */
    public static <C extends Collection<V>, V> boolean isEmpty(C collection) {
        return CollectionUtils.DEFAULT_INSTANCE.isEmpty(collection);
    }

    /**
     * Verify if map has no records.
     *
     * @param map instance to be validated.
     * @param <M> type of map.
     * @param <K> type of key in map.
     * @param <V> type of value in map.
     * @return true if provided map is null or empty, otherwise false.
     */
    public static <M extends Map<K, V>, K, V> boolean isEmpty(M map) {
        return CollectionUtils.DEFAULT_INSTANCE.isEmpty(map);
    }

    /**
     * Verify if array has no records.
     *
     * @param array instance to be validated.
     * @param <V>   type of array
     * @return true if provided array is null or has 0 length, otherwise false.
     */
    public static <V> boolean isEmpty(V[] array) {
        return CollectionUtils.DEFAULT_INSTANCE.isEmpty(array);
    }

    /**
     * Verify if collection has records.
     *
     * @param collection instance to be validated.
     * @param <C>        type of collection.
     * @param <V>        type of value in collection.
     * @return true if provided collection has records, otherwise false.
     */
    public static <C extends Collection<V>, V> boolean isNotEmpty(C collection) {
        return CollectionUtils.DEFAULT_INSTANCE.isNotEmpty(collection);
    }

    /**
     * Verify if map has records.
     *
     * @param map instance to be validated.
     * @param <M> type of map.
     * @param <K> type of key in map.
     * @param <V> type of value in map.
     * @return true if provided map has records, otherwise false.
     */
    public static <M extends Map<K, V>, K, V> boolean isNotEmpty(M map) {
        return CollectionUtils.DEFAULT_INSTANCE.isNotEmpty(map);
    }

    /**
     * Verify if array has records.
     *
     * @param array instance to be validated.
     * @param <V>   type of array
     * @return true if provided array has records, otherwise false.
     */
    public static <V> boolean isNotEmpty(V[] array) {
        return CollectionUtils.DEFAULT_INSTANCE.isNotEmpty(array);
    }

    /**
     * Add safely value to collection, if input collection is null use init to supply new instance of collection.
     *
     * @param collection used as target for adding value.
     * @param value      value to be added into collection.
     * @param <V>        type fo value.
     * @return provided collection ot initialized by ArrayList with added value.
     */
    public static <V> List<V> addList(List<V> collection, V value) {
        return CollectionAddUtils.DEFAULT_INSTANCE.addList(collection, value);
    }

    /**
     * Add safely value to collection, if input collection is null use init to supply new instance of collection.
     *
     * @param collection used as target for adding value.
     * @param value      value to be added into collection.
     * @param <V>        type fo value.
     * @return provided collection ot initialized by HashSet with added value.
     */
    public static <V> Set<V> addSet(Set<V> collection, V value) {
        return CollectionUtils.DEFAULT_INSTANCE.addSet(collection, value);
    }

    /**
     * Add safely value to collection, if input collection is null use init to supply new instance of collection.
     *
     * @param collection used as target for adding value.
     * @param init       supplier to provide new instance in case when provided collection is null.
     * @param value      value to be added into collection.
     * @param <C>        type of collection.
     * @param <V>        type fo value.
     * @return provided collection ot initialized new instance with added value.
     */
    public static <C extends Collection<V>, V> C add(C collection, Supplier<C> init, V value) {
        return CollectionUtils.DEFAULT_INSTANCE.add(collection, init, value);
    }

    /**
     * Add safely values from source collection into destination collection if provided destination is null it is
     * initialized by init supplier.
     *
     * @param collection used as target for adding value.
     * @param values     values to be added into collection.
     * @param <D>        type of destination collection.
     * @param <S>        type of source collection.
     * @param <V>        type of value in collection.
     * @return original destination collection or initialized new collection with added provided values if source was
     *         not empty, otherwise collection value.
     */
    public static <D extends List<V>, S extends Collection<V>, V> List<V> addAllList(
            D collection, S values
    ) {
        return CollectionUtils.DEFAULT_INSTANCE.addAllList(collection, values);
    }

    /**
     * Add safely values from source collection into destination collection if provided destination is null it is
     * initialized by init supplier.
     *
     * @param collection used as target for adding value.
     * @param values     values to be added into collection.
     * @param <D>        type of destination collection.
     * @param <S>        type of source collection.
     * @param <V>        type of value in collection.
     * @return original destination collection or initialized new collection with added provided values if source was
     *         not empty, otherwise collection value.
     */
    public static <D extends Set<V>, S extends Collection<V>, V> Set<V> addAllSet(
            D collection, S values
    ) {
        return CollectionUtils.DEFAULT_INSTANCE.addAllSet(collection, values);
    }

    /**
     * Add safely values from source collection into destination collection if provided destination is null it is
     * initialized by init supplier.
     *
     * @param collection used as target for adding value.
     * @param init       supplier to provide new instance in case when provided collection is null.
     * @param values     values to be added into collection.
     * @param <D>        type of destination collection.
     * @param <S>        type of source collection.
     * @param <V>        type of value in collection.
     * @return original destination collection or initialized new collection with added provided values if source was
     *         not empty, otherwise collection value.
     */
    public static <D extends Collection<V>, S extends Collection<V>, V> D addAll(
            D collection, Supplier<D> init, S values
    ) {
        return CollectionUtils.DEFAULT_INSTANCE.addAll(collection, init, values);
    }

    /**
     * Add safely key and value pair into map, if input map is null init is used to supply new instance.
     *
     * @param map   used as target for adding values.
     * @param key   key connected to value in map.
     * @param value value stored in map connected to key.
     * @param <K>   type of key.
     * @param <V>   type of value.
     * @return provided map or initialized as HashMap with added key and value.
     */
    public static <K, V> Map<K, V> putMap(Map<K, V> map, K key, V value) {
        return CollectionUtils.DEFAULT_INSTANCE.putMap(map, key, value);
    }

    /**
     * Put safely key and value pair into map, if input map is null init is used to supply new instance.
     *
     * @param map   used as target for adding values.
     * @param init  supplier to provided new instance in case when provided map is null.
     * @param key   key connected to value in map.
     * @param value value stored in map connected to key.
     * @param <M>   type of map.
     * @param <K>   type of key.
     * @param <V>   type of value.
     * @return provided map or initialized by supplier with added key and value.
     */
    public static <M extends Map<K, V>, K, V> M put(M map, Supplier<M> init, K key, V value) {
        return CollectionUtils.DEFAULT_INSTANCE.put(map, init, key, value);
    }

    /**
     * Put safely values from source map into target map and when target map is null new instance is created by provided
     * supplier. When source map has no records target parameter is returned without change.
     *
     * @param target map for records provided in values map.
     * @param init   supplier to provided new instance in case when provided map is null.
     * @param source map with source data
     * @param <T>    type of map.
     * @param <K>    type of key.
     * @param <V>    type of value.
     * @return target map or initialized new map with all values from provided values map if it was not empty.
     *         Otherwise, value of target parameter without change.
     */
    public static <T extends Map<K, V>, S extends Map<K, V>, K, V> T putAll(T target, Supplier<T> init, S source) {
        return CollectionUtils.DEFAULT_INSTANCE.putAll(target, init, source);
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
    public static <S extends Map<K, V>, K, V> Map<K, V> putAllMap(Map<K, V> target, S source){
        return CollectionUtils.DEFAULT_INSTANCE.putAllMap(target, source);
    }

    /**
     * Validate if provided collection contains provided value.
     *
     * @param collection collection to be validated for presence of provided value.
     * @param value      value searched in collection for presence.
     * @param <C>        type of collection.
     * @param <V>        type of value.
     * @return true when value is in collection, otherwise false.
     */
    public static <C extends Collection<V>, V> boolean contains(C collection, V value) {
        return CollectionUtils.DEFAULT_INSTANCE.contains(collection, value);
    }

    /**
     * Validate if provided map contains provided key value.
     *
     * @param map map to be validated for presence of provided key value.
     * @param key value of key search in map for presence.
     * @param <M> type of map.
     * @param <K> type of key.
     * @param <V> type of value.
     * @return true in case when key is present in map.
     */
    public static <M extends Map<K, V>, K, V> boolean contains(M map, K key) {
        return CollectionUtils.DEFAULT_INSTANCE.contains(map, key);
    }

    /**
     * Validate if provided map contains provided value assigned to key.
     *
     * @param map   map to be validated for presence of provided value.
     * @param value value search in map values.
     * @param <M>   type of map.
     * @param <K>   type of key.
     * @param <V>   type of value.
     * @return true in case when value is present in map.
     */
    public static <M extends Map<K, V>, K, V> boolean containsValue(M map, V value) {
        return CollectionUtils.DEFAULT_INSTANCE.containsValue(map, value);
    }

    /**
     * Validate safely if provided target contains all provided values. In case when empty or null source is provided it
     * is considered as false result.
     *
     * @param target collection to be validated if contains all provided records.
     * @param source collection with values used for validation.
     * @param <T>    type of target collection.
     * @param <S>    type of source collection.
     * @param <V>    type of values in collection.
     * @return true in case when target contains all values provided in source, otherwise false.
     */
    public static <T extends Collection<V>, S extends Collection<V>, V> boolean containsAll(T target, S source) {
        return CollectionUtils.DEFAULT_INSTANCE.containsAll(target, source);
    }

    /**
     * Validate safely if provided target contains all provided entries. In case when empty or null source is provided
     * it is considered as false result.
     *
     * @param target map to be validated if it contains all same entries provided in source.
     * @param source map of map entries for validation.
     * @param <T>    type of target map.
     * @param <S>    type of source map.
     * @param <K>    type of key in map.
     * @param <V>    type of value in map.
     * @return true in case when target contains all same key value pairs, otherwise false.
     */
    public static <T extends Map<K, V>, S extends Map<K, V>, K, V> boolean containsAll(T target, S source) {
        return CollectionUtils.DEFAULT_INSTANCE.containsAll(target, source);
    }

    /**
     * Validate safely if provided target contains all provided keys. In case when empty or null source is provided it
     * is considered as false result.
     *
     * @param target map to be validated if it contains all keys provided in source.
     * @param source set of key value for validation.
     * @param <T>    type of target map.
     * @param <S>    type of source map.
     * @param <K>    type of key in map.
     * @param <V>    type of value in map.
     * @return true in case when target contains all same key value pairs, otherwise false.
     */
    public static <T extends Map<K, V>, S extends Set<K>, K, V> boolean containsAll(T target, S source){
        return CollectionUtils.DEFAULT_INSTANCE.containsAll(target, source);
    }

    /**
     * Remove value from provided collection and return true in case when value was removed.
     *
     * @param collection used as source of data for value removal.
     * @param value      value to be removed from collection.
     * @param <C>        type of collection.
     * @param <V>        type of value.
     * @return true in case when value was removed from collection, otherwise false
     */
    public static <C extends Collection<V>, V> boolean remove(C collection, V value) {
        return CollectionUtils.DEFAULT_INSTANCE.remove(collection, value);
    }

    /**
     * Remove key and value pair from provided collection and return true in case when value was removed.
     *
     * @param map   used to remove data.
     * @param key   key part of entry to be removed from map.
     * @param value value part of entry to be removed from map.
     * @param <M>   type of map.
     * @param <K>   type of key.
     * @param <V>   type of value.
     * @return true in case when key and value was removed from map, otherwise false
     */
    public static <M extends Map<K, V>, K, V> boolean remove(M map, K key, V value) {
        return CollectionUtils.DEFAULT_INSTANCE.remove(map, key, value);
    }

    /**
     * Remove key and value pair from provided collection and return true in case when value was removed.
     *
     * @param map used to remove data.
     * @param key key part of entry to be removed from map.
     * @param <M> type of map.
     * @param <K> type of key.
     * @param <V> type of value.
     * @return true in case when key and value was removed from map, otherwise false
     */
    public static <M extends Map<K, V>, K, V> boolean remove(M map, K key) {
        return CollectionUtils.DEFAULT_INSTANCE.remove(map, key);
    }

    /**
     * Remove all matching records from collection contained in values collection.
     *
     * @param collection collection where values should be removed.
     * @param values     values to be removed from collection.
     * @param <S>        type of modified collection.
     * @param <D>        type of data collection.
     * @param <V>        type of value stored in collection.
     * @return true in case when collection is modified by provided values, otherwise false.
     */
    public static <S extends Collection<V>, D extends Collection<V>, V> boolean removeAll(S collection, D values) {
        return CollectionUtils.DEFAULT_INSTANCE.removeAll(collection, values);
    }

    /**
     * Remove all matching key value paris from map.
     *
     * @param map    for modification by removal.
     * @param remove map with key pairs to be removed from map.
     * @param <D>    type of destination map.
     * @param <S>    type of data source map.
     * @param <K>    type of key.
     * @param <V>    type of value.
     * @return m true when map is modified by removal, otherwise false.
     */
    public static <D extends Map<K, V>, S extends Map<K, V>, K, V> boolean removeAll(D map, S remove) {
        return CollectionUtils.DEFAULT_INSTANCE.removeAll(map, remove);
    }

    /**
     * Remove all matching keys from target map located in source map.
     *
     * @param target for modification by removal.
     * @param source map with key pairs to be removed from map.
     * @return m true when map is modified by removal, otherwise false.
     */
    public static <T extends Map<K, V>, S extends Set<K>, K, V> boolean removeAll(T target, S source){
        return CollectionUtils.DEFAULT_INSTANCE.removeAll(target, source);
    }
    /**
     * Retain all values existing in collection and provided values.
     *
     * @param target collection to be modified.
     * @param values values to retain in original collection.
     * @param <S>    type of modified collection
     * @param <D>    type of retain data collection
     * @param <V>    type of value in collections.
     * @return true if target collection was modified
     */
    public static <S extends Collection<V>, D extends Collection<V>, V> boolean retainAll(S target, D values) {
        return CollectionUtils.DEFAULT_INSTANCE.retainAll(target, values);
    }
}
