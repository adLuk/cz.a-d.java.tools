package cz.ad.java.tools.collection;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Implement interface for basic operation with collection supporting null instances of used collection in operation and
 * provides basic validation and operation with collection including initialization.
 */
public class DefaultCollectionUtils implements CollectionAddUtils {
    /**
     * Verify if collection has no records.
     *
     * @param collection instance to be validated.
     * @return true if provided collection is null or empty, otherwise false.
     */
    @Override
    public <C extends Collection<V>, V> boolean isEmpty(C collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * Verify if map has no records.
     *
     * @param map instance to be validated.
     * @return true if provided map is null or empty, otherwise false.
     */
    @Override
    public <M extends Map<K, V>, K, V> boolean isEmpty(M map) {
        return map == null || map.isEmpty();
    }

    /**
     * Verify if array has no records.
     *
     * @param array instance to be validated.
     * @return true if provided array is null or has 0 length, otherwise false.
     */
    @Override
    public <V> boolean isEmpty(V[] array) {
        return (array == null || array.length == 0);
    }

    /**
     * Verify if collection has records.
     *
     * @param collection instance to be validated.
     * @return true if provided collection has records, otherwise false.
     */
    @Override
    public <C extends Collection<V>, V> boolean isNotEmpty(C collection) {
        return collection != null && !collection.isEmpty();
    }

    /**
     * Verify if map has records.
     *
     * @param map instance to be validated.
     * @return true if provided map has records, otherwise false.
     */
    @Override
    public <M extends Map<K, V>, K, V> boolean isNotEmpty(M map) {
        return map != null && !map.isEmpty();
    }

    /**
     * Verify if array has records.
     *
     * @param array instance to be validated.
     * @return true if provided array has records, otherwise false.
     */
    @Override
    public <V> boolean isNotEmpty(V[] array) {
        return (array != null && array.length > 0);
    }

    /**
     * Add safely value to collection, if input collection is null use init to supply new instance of collection.
     *
     * @param collection used as target for adding value.
     * @param init       supplier to provide new instance in case when provided collection is null.
     * @param value      value to be added into collection.
     * @return provided collection or initialized new instance with added value.
     */
    @Override
    public <C extends Collection<V>, V> C add(C collection, Supplier<C> init, V value) {
        C retValue = collection;
        if (retValue == null) {
            retValue = init.get();
        }
        if (retValue != null) {
            retValue.add(value);
        }
        return retValue;
    }

    /**
     * Add safely values from source collection into destination collection if provided destination is null it is
     * initialized by init supplier. If provided source is null or empty target without change is return.
     *
     * @param target used as target for adding value.
     * @param init   supplier to provide new instance in case when provided collection is null.
     * @param source values to be added into collection.
     * @return original target collection or initialized new collection with added provided values if source was not
     *         empty, otherwise target collection parameter value without change.
     */
    @Override
    public <T extends Collection<V>, S extends Collection<V>, V> T addAll(T target, Supplier<T> init, S source) {
        T retValue = target;
        if (isNotEmpty(source)) {
            if (retValue == null) {
                retValue = init.get();
            }
            if (retValue != null) {
                retValue.addAll(source);
            }
        }
        return retValue;
    }

    /**
     * Put safely key and value pair into map, if input map is null init is used to supply new instance.
     *
     * @param map   used as target for adding values.
     * @param init  supplier to provided new instance in case when provided map is null.
     * @param key   key connected to value in map.
     * @param value value stored in map connected to key.
     * @return provided map or initialized by supplier with added key and value.
     */
    @Override
    public <M extends Map<K, V>, K, V> M put(M map, Supplier<M> init, K key, V value) {
        M retValue = map;
        if (retValue == null) {
            retValue = init.get();
        }
        if (retValue != null) {
            retValue.put(key, value);
        }
        return retValue;
    }

    /**
     * Put safely values from source map into target map and when target map is null new instance is created by provided
     * supplier. When source map has no records target parameter is returned without change.
     *
     * @param target map for records provided in values map.
     * @param init   supplier to provided new instance in case when provided map is null.
     * @param source map with source data
     * @return target map or initialized new map with all values from provided values map if it was not empty.
     *         Otherwise, value of target parameter without change.
     */
    @Override
    public <T extends Map<K, V>, S extends Map<K, V>, K, V> T putAll(T target, Supplier<T> init, S source) {
        T retValue = target;
        if (isNotEmpty(source)) {
            if (retValue == null) {
                retValue = init.get();
            }
            if (retValue != null) {
                retValue.putAll(source);
            }
        }
        return retValue;
    }

    /**
     * Validate if provided collection contains provided value.
     *
     * @param collection collection to be validated for presence of provided value.
     * @param value      value searched in collection for presence.
     * @return true when value is in collection, otherwise false.
     */
    @Override
    public <C extends Collection<V>, V> boolean contains(C collection, V value) {
        return collection != null && collection.contains(value);
    }

    /**
     * Validate if provided map contains provided key value.
     *
     * @param map map to be validated for presence of provided key value.
     * @param key value of key search in map for presence.
     * @return true in case when key is present in map.
     */
    @Override
    public <M extends Map<K, V>, K, V> boolean contains(M map, K key) {
        return map != null && map.containsKey(key);
    }

    /**
     * Validate if provided map contains provided value assigned to key.
     *
     * @param map   map to be validated for presence of provided value.
     * @param value value search in map values.
     * @return true in case when value is present in map.
     */
    @Override
    public <M extends Map<K, V>, K, V> boolean containsValue(M map, V value) {
        return map != null && map.containsValue(value);
    }

    /**
     * Validate safely if provided target contains all provided values. In case when empty or null source is provided it
     * is considered as false result.
     *
     * @param target collection to be validated if contains all provided records.
     * @param source collection with values used for validation.
     * @return true in case when target contains all values provided in source, otherwise false.
     */
    @Override
    public <T extends Collection<V>, S extends Collection<V>, V> boolean containsAll(T target, S source) {
        return target != null && isNotEmpty(source) && target.containsAll(source);
    }

    /**
     * Validate safely if provided target contains all provided entries. In case when empty or null source is provided
     * it is considered as false result.
     *
     * @param target map to be validated if it contains all same entries provided in source.
     * @param source map of map entries for validation.
     * @return true in case when target contains all same key value pairs, otherwise false.
     */
    @Override
    public <T extends Map<K, V>, S extends Map<K, V>, K, V> boolean containsAll(T target, S source) {
        boolean retValue = false;
        if (isNotEmpty(source) && isNotEmpty(target)) {
            retValue = true;
            for (Map.Entry<K, V> entry : source.entrySet()) {
                if (target.containsKey(entry.getKey())) {
                    V value = target.get(entry.getKey());
                    if (!value.equals(entry.getValue())) {
                        retValue = false;
                        break;
                    }
                } else {
                    retValue = false;
                    break;
                }
            }
        }
        return retValue;
    }

    /**
     * Validate safely if provided target contains all provided keys. In case when empty or null source is provided it
     * is considered as false result.
     *
     * @param target map to be validated if it contains all keys provided in source.
     * @param source set of key value for validation.
     * @return true in case when target contains all same key value pairs, otherwise false.
     */
    @Override
    public <T extends Map<K, V>, S extends Set<K>, K, V> boolean containsAll(T target, S source) {
        boolean retValue = false;
        if (isNotEmpty(source) && isNotEmpty(target)) {
            retValue = true;
            for (K key : source) {
                if (!target.containsKey(key)) {
                    retValue = false;
                    break;
                }
            }
        }
        return retValue;
    }

    /**
     * Remove value from provided collection and return true in case when value was removed.
     *
     * @param collection used as source of data for value removal.
     * @param value      value to be removed from collection.
     * @return true in case when value was removed from collection, otherwise false
     */
    @Override
    public <C extends Collection<V>, V> boolean remove(C collection, V value) {
        return collection != null && collection.remove(value);
    }

    /**
     * Remove key and value map entry from provided collection and return true in case when value was removed.
     *
     * @param map   used to remove data.
     * @param key   key part of entry to be removed from map.
     * @param value value part of entry to be removed from map.
     * @return true in case when key and value was removed from map, otherwise false
     */
    @Override
    public <M extends Map<K, V>, K, V> boolean remove(M map, K key, V value) {
        return map != null && map.remove(key, value);
    }

    /**
     * Remove map entry from provided target map and return true in case when value was removed.
     *
     * @param map used to remove data.
     * @param key key for entry to be removed from map.
     * @return true in case when key and value was removed from map, otherwise false
     */
    @Override
    public <M extends Map<K, V>, K, V> boolean remove(M map, K key) {
        boolean retValue = false;
        if (isNotEmpty(map)) {
            if (map.containsKey(key)) {
                map.remove(key);
                retValue = true;
            }
        }
        return retValue;
    }

    /**
     * Remove all matching records from collection contained in values collection.
     *
     * @param target collection where values should be removed.
     * @param source values to be removed from collection.
     * @return true in case when collection is modified by provided values, otherwise false.
     */
    @Override
    public <T extends Collection<V>, S extends Collection<V>, V> boolean removeAll(T target, S source) {
        boolean retValue = false;
        if (isNotEmpty(source) && isNotEmpty(target)) {
            retValue = target.removeAll(source);
        }
        return retValue;
    }

    /**
     * Remove all matching entries from target map located in source map.
     *
     * @param target for modification by removal.
     * @param source map with key pairs to be removed from map.
     * @return m true when map is modified by removal, otherwise false.
     */
    @Override
    public <T extends Map<K, V>, S extends Map<K, V>, K, V> boolean removeAll(T target, S source) {
        boolean retValue = false;
        if (isNotEmpty(source) && isNotEmpty(target)) {
            for (Map.Entry<K, V> entry : source.entrySet()) {
                if (target.remove(entry.getKey(), entry.getValue())) {
                    retValue = true;
                }
            }
        }
        return retValue;
    }

    /**
     * Remove all matching keys from target map located in source map.
     *
     * @param target for modification by removal.
     * @param source map with key pairs to be removed from map.
     * @return m true when map is modified by removal, otherwise false.
     */
    @Override
    public <T extends Map<K, V>, S extends Set<K>, K, V> boolean removeAll(T target, S source) {
        boolean retValue = false;
        if (isNotEmpty(source) && isNotEmpty(target)) {
            for (K key : source) {
                if (target.containsKey(key)) {
                    target.remove(key);
                    retValue = true;
                }
            }
        }
        return retValue;
    }

    /**
     * Retain all values existing in collection and provided values.
     *
     * @param target collection to be modified.
     * @param source values to retain in original collection.
     * @return true if target collection was modified
     */
    @Override
    public <T extends Collection<V>, S extends Collection<V>, V> boolean retainAll(T target, S source) {
        boolean retValue = false;
        if (isNotEmpty(target) && isNotEmpty(source)) {
            retValue = target.retainAll(source);
        }
        return retValue;
    }

    @Override
    public String toString() {
        return "DefaultCollectionUtils{}";
    }
}
