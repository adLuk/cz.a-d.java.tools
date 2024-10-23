package cz.ad.java.tools.collection.bean;

import cz.ad.java.tools.collection.CollectionAddUtils;
import cz.ad.java.tools.collection.CollectionUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Collection utils interface supporting direct operations with beans members.
 */
public interface BeanCollectionUtils {

    /**
     * Provide single shared default instance of collection utils.
     */
    BeanCollectionUtils DEFAULT_INSTANCE = instance(CollectionUtils.DEFAULT_INSTANCE);

    /**
     * Create new instance of collection utils.
     *
     * @return new instance of collection utils.
     */
    static BeanCollectionUtils instance(CollectionAddUtils utils) {
        return new DefaultBeanCollectionUtils(utils);
    }

    /**
     * Create new instance of collection utils.
     *
     * @return new instance of collection utils.
     */
    static BeanCollectionUtils instance() {
        return new DefaultBeanCollectionUtils();
    }

    /**
     * Verify if bean collection member accessed by supplier has no records.
     *
     * @param getter supplier providing access to collection member in bean.
     * @param <C>    type of collection.
     * @param <V>    type of value in collection.
     * @return true if provided collection is null or empty, otherwise false.
     */
    <C extends Collection<V>, V> boolean isEmpty(Supplier<C> getter);

    /**
     * Verify if bean map member accessed by supplier has no records.
     *
     * @param getter supplier providing access to map member in bean.
     * @param <M>    type of map.
     * @param <K>    type of key in map.
     * @param <V>    type of value in map.
     * @return true if provided map is null or empty, otherwise false.
     */
    <M extends Map<K, V>, K, V> boolean isMapEmpty(Supplier<M> getter);


    /**
     * Verify if bean array member accessed by supplier has no records.
     *
     * @param getter supplier providing access to array member in bean.
     * @param <V>    type of array
     * @return true if provided array is null or has 0 length, otherwise false.
     */
    <V> boolean isArrayEmpty(Supplier<V[]> getter);

    /**
     * Verify if bean collection member accessed by supplier has records.
     *
     * @param getter supplier providing access to collection member in bean.
     * @param <C>    type of collection.
     * @param <V>    type of value in collection.
     * @return true if provided collection has records, otherwise false.
     */
    <C extends Collection<V>, V> boolean isNotEmpty(Supplier<C> getter);

    /**
     * Verify if bean map member accessed by supplier has records.
     *
     * @param getter supplier providing access to map member in bean.
     * @param <M>    type of map.
     * @param <K>    type of key in map.
     * @param <V>    type of value in map.
     * @return true if provided map has records, otherwise false.
     */
    <M extends Map<K, V>, K, V> boolean isMapNotEmpty(Supplier<M> getter);

    /**
     * Verify if bean array member accessed by supplier has records.
     *
     * @param getter instance to be validated.
     * @param <V>    type of array
     * @return true if provided array has records, otherwise false.
     */
    <V> boolean isArrayNotEmpty(Supplier<V[]> getter);

    /**
     * Add safely value into bean collection member accessed by provided getter, if input collection is null use init to
     * supply new instance of collection.
     *
     * @param getter supplier providing access to collection member in bean.
     * @param setter consumer used in case when bean member is null and needs to be initialized.
     * @param init   supplier to provide new instance in case when provided collection is null.
     * @param value  value to be added into collection.
     * @param <C>    type of collection.
     * @param <V>    type fo value.
     * @return provided collection or initialized new instance with added value.
     */
    <C extends Collection<V>, V> C add(Supplier<C> getter, Consumer<C> setter, Supplier<C> init, V value);

    /**
     * Add safely values from source collection into bean collection member accessed by provided getter if provided
     * destination is null it is initialized by init supplier. If provided source is null or empty target without change
     * is return.
     *
     * @param getter supplier providing access to collection member in bean.
     * @param setter consumer used in case when bean member is null and needs to be initialized.
     * @param init   supplier to provide new instance in case when provided collection is null.
     * @param source values to be added into collection.
     * @param <T>    type of destination collection.
     * @param <S>    type of source collection.
     * @param <V>    type of value in collection.
     * @return original target collection or initialized new collection with added provided values if source was not
     *         empty, otherwise target collection parameter value without change.
     */
    <T extends Collection<V>, S extends Collection<V>, V> T addAll(
            Supplier<T> getter, Consumer<T> setter, Supplier<T> init, S source
    );

    /**
     * Put safely key and value pair into bean map member accessed by provided getter, if input map is null init is used
     * to supply new instance.
     *
     * @param getter supplier providing access to map member in bean.
     * @param setter consumer used in case when bean member is null and needs to be initialized.
     * @param init   supplier to provided new instance in case when provided map is null.
     * @param key    key connected to value in map.
     * @param value  value stored in map connected to key.
     * @param <M>    type of map.
     * @param <K>    type of key.
     * @param <V>    type of value.
     * @return provided map or initialized by supplier with added key and value.
     */
    <M extends Map<K, V>, K, V> M put(Supplier<M> getter, Consumer<M> setter, Supplier<M> init, K key, V value);

    /**
     * Put safely values from source map into bean map member accessed by provided getter and when target map is null
     * new instance is created by provided supplier. When source map has no records target parameter is returned without
     * change.
     *
     * @param getter supplier providing access to map member in bean.
     * @param setter consumer used in case when bean member is null and needs to be initialized.
     * @param init   supplier to provided new instance in case when provided map is null.
     * @param source map with source data
     * @param <T>    type of target map.
     * @param <S>    type of source map.
     * @param <K>    type of key.
     * @param <V>    type of value.
     * @return target map or initialized new map with all values from provided values map if it was not empty.
     *         Otherwise, value of target parameter without change.
     */
    <T extends Map<K, V>, S extends Map<K, V>, K, V> T putAll(
            Supplier<T> getter, Consumer<T> setter, Supplier<T> init, S source
    );

    /**
     * Validate safely if collection member provided by getter contains value.
     *
     * @param getter supplier providing access to collection member in bean.
     * @param value  value searched in collection for presence.
     * @param <C>    type of collection.
     * @param <V>    type of value.
     * @return true when value is in collection, otherwise false.
     */
    <C extends Collection<V>, V> boolean contains(Supplier<C> getter, V value);

    /**
     * Validate safely if map member provided by getter contains key.
     *
     * @param getter supplier providing access to map member in bean.
     * @param key    key value search in map for presence.
     * @param <M>    type of map.
     * @param <K>    type of key.
     * @param <V>    type of value.
     * @return true in case when key is present in map.
     */
    <M extends Map<K, V>, K, V> boolean containsKey(Supplier<M> getter, K key);

    /**
     * Validate safely if map member provided by getter contains value.
     *
     * @param getter supplier providing access to map member in bean.
     * @param value  value search in map values.
     * @param <M>    type of map.
     * @param <K>    type of key.
     * @param <V>    type of value.
     * @return true in case when value is present in map.
     */
    <M extends Map<K, V>, K, V> boolean containsValue(Supplier<M> getter, V value);

    /**
     * Validate safely if collection member provided by getter contains all provided values. In case when empty or null
     * source is provided it is considered as false result.
     *
     * @param getter supplier providing access to collection member in bean.
     * @param source collection with values used for validation.
     * @param <T>    type of target collection.
     * @param <S>    type of source collection.
     * @param <V>    type of values in collection.
     * @return true in case when target contains all values provided in source, otherwise false.
     */
    <T extends Collection<V>, S extends Collection<V>, V> boolean containsAll(Supplier<T> getter, S source);

    /**
     * Validate safely if collection member provided by getter contains all provided entries. In case when empty or null
     * source is provided it is considered as false result.
     *
     * @param getter supplier providing access to map member in bean.
     * @param source map of map entries for validation.
     * @param <T>    type of target map.
     * @param <S>    type of source map.
     * @param <K>    type of key in map.
     * @param <V>    type of value in map.
     * @return true in case when target contains all same key value pairs, otherwise false.
     */
    <T extends Map<K, V>, S extends Map<K, V>, K, V> boolean containsAll(Supplier<T> getter, S source);

    /**
     * Validate safely if collection member provided by getter contains all provided keys. In case when empty or null
     * source is provided it is considered as false result.
     *
     * @param getter supplier providing access to map member in bean.
     * @param source set of key value for validation.
     * @param <T>    type of target map.
     * @param <S>    type of source map.
     * @param <K>    type of key in map.
     * @param <V>    type of value in map.
     * @return true in case when target contains all same key value pairs, otherwise false.
     */
    <T extends Map<K, V>, S extends Set<K>, K, V> boolean containsAll(Supplier<T> getter, S source);

    /**
     * Remove value from collection member provided by getter and return true in case when value was removed.
     *
     * @param getter supplier providing access to collection member in bean.
     * @param value  value to be removed from collection.
     * @param <C>    type of collection.
     * @param <V>    type of value.
     * @return true in case when value was removed from collection, otherwise false
     */
    <C extends Collection<V>, V> boolean remove(Supplier<C> getter, V value);

    /**
     * Remove key and value map entry from map member provided by getter and return true in case when value was
     * removed.
     *
     * @param getter supplier providing access to map member in bean.
     * @param key    key part of entry to be removed from map.
     * @param value  value part of entry to be removed from map.
     * @param <M>    type of map.
     * @param <K>    type of key.
     * @param <V>    type of value.
     * @return true in case when key and value was removed from map, otherwise false
     */
    <M extends Map<K, V>, K, V> boolean remove(Supplier<M> getter, K key, V value);

    /**
     * Remove map entry from map member provided by getter and return true in case when value was removed.
     *
     * @param getter supplier providing access to map member in bean.
     * @param key    key for entry to be removed from map.
     * @param <M>    type of map.
     * @param <K>    type of key.
     * @param <V>    type of value.
     * @return true in case when key and value was removed from map, otherwise false
     */
    <M extends Map<K, V>, K, V> boolean removeKey(Supplier<M> getter, K key);

    /**
     * Remove all matching records from collection member provided by getter contained in values source collection.
     *
     * @param getter supplier providing access to map member in bean.
     * @param source values to be removed from collection.
     * @param <T>    type of modified collection.
     * @param <S>    type of data collection.
     * @param <V>    type of value stored in collection.
     * @return true in case when collection is modified by provided values, otherwise false.
     */
    <T extends Collection<V>, S extends Collection<V>, V> boolean removeAll(Supplier<T> getter, S source);

    /**
     * Remove all matching entries from map member provided by getter located in source map.
     *
     * @param getter supplier providing access to map member in bean.
     * @param source map with key pairs to be removed from map.
     * @param <T>    type of destination map.
     * @param <S>    type of data source map.
     * @param <K>    type of key.
     * @param <V>    type of value.
     * @return m true when map is modified by removal, otherwise false.
     */
    <T extends Map<K, V>, S extends Map<K, V>, K, V> boolean removeAll(Supplier<T> getter, S source);

    /**
     * Remove all matching keys from target map member provided by getter located in source map.
     *
     * @param getter supplier providing access to map member in bean.
     * @param source map with key pairs to be removed from map.
     * @param <T>    type of destination map.
     * @param <S>    type of data source map.
     * @param <K>    type of key.
     * @param <V>    type of value.
     * @return m true when map is modified by removal, otherwise false.
     */
    <T extends Map<K, V>, S extends Set<K>, K, V> boolean removeAll(Supplier<T> getter, S source);

    /**
     * Retain all values existing in collection member provided by getter and provided values.
     *
     * @param getter supplier providing access to map member in bean.
     * @param source values to retain in original collection.
     * @param <T>    type of modified collection
     * @param <S>    type of retain data collection
     * @param <V>    type of value in collections.
     * @return true if target collection was modified
     */
    <T extends Collection<V>, S extends Collection<V>, V> boolean retainAll(Supplier<T> getter, S source);
}
