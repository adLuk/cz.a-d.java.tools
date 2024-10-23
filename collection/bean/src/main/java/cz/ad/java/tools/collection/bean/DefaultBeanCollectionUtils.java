package cz.ad.java.tools.collection.bean;

import cz.ad.java.tools.collection.core.CollectionAddUtils;
import cz.ad.java.tools.collection.core.CollectionUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Default implementation of bean oriented collection utils.
 */
public class DefaultBeanCollectionUtils implements BeanCollectionAddUtils {
    /**
     * Store instance of internally used collection utils.
     */
    protected CollectionAddUtils collectionUtils;

    /**
     * Constructor allow to initialize new instance provided in interface by shared default instance of CollectionUtils.
     * Must not be null.
     *
     * @param collectionAddUtils instance of collection utils used to internally to perform operation on the top of
     *                           collections.  Must not be null.
     * @throws IllegalArgumentException when collection utils instance provided as parameter is null.
     */
    public DefaultBeanCollectionUtils(CollectionAddUtils collectionAddUtils) throws IllegalArgumentException {
        if (collectionAddUtils == null) {
            throw new IllegalArgumentException("CollectionAddUtils must not be null");
        }
        this.collectionUtils = collectionAddUtils;
    }

    /**
     * Default constructor initialize new internal instance of Collection utils.
     */
    public DefaultBeanCollectionUtils() {
        collectionUtils = CollectionUtils.instance();
    }

    /**
     * Verify if bean collection member accessed by supplier has no records.
     *
     * @param getter supplier providing access to collection member in bean.
     * @return true if provided collection is null or empty, otherwise false.
     */
    @Override
    public <C extends Collection<V>, V> boolean isEmpty(Supplier<C> getter) {
        boolean retValue = true;
        if (getter != null) {
            C collection = getter.get();
            retValue = collectionUtils.isEmpty(collection);
        }
        return retValue;
    }

    /**
     * Verify if bean map member accessed by supplier has no records.
     *
     * @param getter supplier providing access to map member in bean.
     * @return true if provided map is null or empty, otherwise false.
     */
    @Override
    public <M extends Map<K, V>, K, V> boolean isMapEmpty(Supplier<M> getter) {
        boolean retValue = true;
        if (getter != null) {
            M map = getter.get();
            retValue = collectionUtils.isEmpty(map);
        }
        return retValue;
    }

    /**
     * Verify if bean array member accessed by supplier has no records.
     *
     * @param getter supplier providing access to array member in bean.
     * @return true if provided array is null or has 0 length, otherwise false.
     */
    @Override
    public <V> boolean isArrayEmpty(Supplier<V[]> getter) {
        boolean retValue = true;
        if (getter != null) {
            V[] array = getter.get();
            retValue = collectionUtils.isEmpty(array);
        }
        return retValue;
    }

    /**
     * Verify if bean collection member accessed by supplier has records.
     *
     * @param getter supplier providing access to collection member in bean.
     * @return true if provided collection has records, otherwise false.
     */
    @Override
    public <C extends Collection<V>, V> boolean isNotEmpty(Supplier<C> getter) {
        boolean retValue = false;
        if (getter != null) {
            C collection = getter.get();
            retValue = collectionUtils.isNotEmpty(collection);
        }
        return retValue;
    }

    /**
     * Verify if bean map member accessed by supplier has records.
     *
     * @param getter supplier providing access to map member in bean.
     * @return true if provided map has records, otherwise false.
     */
    @Override
    public <M extends Map<K, V>, K, V> boolean isMapNotEmpty(Supplier<M> getter) {
        boolean retValue = false;
        if (getter != null) {
            M map = getter.get();
            retValue = collectionUtils.isNotEmpty(map);
        }
        return retValue;
    }

    /**
     * Verify if bean array member accessed by supplier has records.
     *
     * @param getter instance to be validated.
     * @return true if provided array has records, otherwise false.
     */
    @Override
    public <V> boolean isArrayNotEmpty(Supplier<V[]> getter) {
        boolean retValue = false;
        if (getter != null) {
            V[] array = getter.get();
            retValue = collectionUtils.isNotEmpty(array);
        }
        return retValue;
    }

    /**
     * Add safely value into bean collection member accessed by provided getter, if input collection is null use init to
     * supply new instance of collection.
     *
     * @param getter supplier providing access to collection member in bean.
     * @param setter consumer used in case when bean member is null and needs to be initialized.
     * @param init   supplier to provide new instance in case when provided collection is null.
     * @param value  value to be added into collection.
     * @return provided collection or initialized new instance with added value.
     */
    @Override
    public <C extends Collection<V>, V> C add(Supplier<C> getter, Consumer<C> setter, Supplier<C> init, V value) {
        C retValue = initIfNeeded(getter, setter, init);
        if (retValue != null) {
            retValue.add(value);
        }
        return retValue;
    }

    /**
     * Add safely values from source collection into bean collection member accessed by provided getter if provided
     * destination is null it is initialized by init supplier. If provided source is null or empty target without change
     * is return.
     *
     * @param getter supplier providing access to collection member in bean.
     * @param setter consumer used in case when bean member is null and needs to be initialized.
     * @param init   supplier to provide new instance in case when provided collection is null.
     * @param source values to be added into collection.
     * @return original target collection or initialized new collection with added provided values if source was not
     *         empty, otherwise target collection parameter value without change.
     */
    @Override
    public <T extends Collection<V>, S extends Collection<V>, V> T addAll(
            Supplier<T> getter, Consumer<T> setter, Supplier<T> init, S source
    ) {
        T retValue = null;
        if (collectionUtils.isNotEmpty(source)) {
            retValue = initIfNeeded(getter, setter, init);
            if (retValue != null) {
                retValue.addAll(source);
            }
        }
        return retValue;
    }

    /**
     * Put safely key and value pair into bean map member accessed by provided getter, if input map is null init is used
     * to supply new instance.
     *
     * @param getter supplier providing access to map member in bean.
     * @param setter consumer used in case when bean member is null and needs to be initialized.
     * @param init   supplier to provided new instance in case when provided map is null.
     * @param key    key connected to value in map.
     * @param value  value stored in map connected to key.
     * @return provided map or initialized by supplier with added key and value.
     */
    @Override
    public <M extends Map<K, V>, K, V> M put(Supplier<M> getter, Consumer<M> setter, Supplier<M> init, K key, V value) {
        M retValue = initMapIfNeeded(getter, setter, init);
        if (retValue != null) {
            retValue.put(key, value);
        }
        return retValue;
    }

    /**
     * Put safely values from source map into bean map member accessed by provided getter and when target map is null
     * new instance is created by provided supplier. When source map has no records target parameter is returned without
     * change.
     *
     * @param getter supplier providing access to map member in bean.
     * @param setter consumer used in case when bean member is null and needs to be initialized.
     * @param init   supplier to provided new instance in case when provided map is null.
     * @param source map with source data
     * @return target map or initialized new map with all values from provided values map if it was not empty.
     *         Otherwise, value of target parameter without change.
     */
    @Override
    public <T extends Map<K, V>, S extends Map<K, V>, K, V> T putAll(
            Supplier<T> getter, Consumer<T> setter, Supplier<T> init, S source
    ) {
        T retValue = null;
        if (collectionUtils.isNotEmpty(source)) {
            retValue = initMapIfNeeded(getter, setter, init);
            if (retValue != null) {
                retValue.putAll(source);
            }
        }
        return retValue;
    }

    /**
     * Validate safely if collection member provided by getter contains value.
     *
     * @param getter supplier providing access to collection member in bean.
     * @param value  value searched in collection for presence.
     * @return true when value is in collection, otherwise false.
     */
    @Override
    public <C extends Collection<V>, V> boolean contains(Supplier<C> getter, V value) {
        boolean retValue = false;
        if (getter != null) {
            retValue = collectionUtils.contains(getter.get(), value);
        }
        return retValue;
    }

    /**
     * Validate safely if map member provided by getter contains key.
     *
     * @param getter supplier providing access to map member in bean.
     * @param key    key value search in map for presence.
     * @return true in case when key is present in map.
     */
    @Override
    public <M extends Map<K, V>, K, V> boolean containsKey(Supplier<M> getter, K key) {
        boolean retValue = false;
        if (getter != null) {
            retValue = collectionUtils.contains(getter.get(), key);
        }
        return retValue;
    }

    /**
     * Validate safely if map member provided by getter contains value.
     *
     * @param getter supplier providing access to map member in bean.
     * @param value  value search in map values.
     * @return true in case when value is present in map.
     */
    @Override
    public <M extends Map<K, V>, K, V> boolean containsValue(Supplier<M> getter, V value) {
        boolean retValue = false;
        if (getter != null) {
            retValue = collectionUtils.containsValue(getter.get(), value);
        }
        return retValue;
    }

    /**
     * Validate safely if collection member provided by getter contains all provided values. In case when empty or null
     * source is provided it is considered as false result.
     *
     * @param getter supplier providing access to collection member in bean.
     * @param source collection with values used for validation.
     * @return true in case when target contains all values provided in source, otherwise false.
     */
    @Override
    public <T extends Collection<V>, S extends Collection<V>, V> boolean containsAll(Supplier<T> getter, S source) {
        boolean retValue = false;
        if (getter != null) {
            retValue = collectionUtils.containsAll(getter.get(), source);
        }
        return retValue;
    }

    /**
     * Validate safely if collection member provided by getter contains all provided entries. In case when empty or null
     * source is provided it is considered as false result.
     *
     * @param getter supplier providing access to map member in bean.
     * @param source map of map entries for validation.
     * @return true in case when target contains all same key value pairs, otherwise false.
     */
    @Override
    public <T extends Map<K, V>, S extends Map<K, V>, K, V> boolean containsAll(Supplier<T> getter, S source) {
        boolean retValue = false;
        if (getter != null) {
            retValue = collectionUtils.containsAll(getter.get(), source);
        }
        return retValue;
    }

    /**
     * Validate safely if collection member provided by getter contains all provided keys. In case when empty or null
     * source is provided it is considered as false result.
     *
     * @param getter supplier providing access to map member in bean.
     * @param source set of key value for validation.
     * @return true in case when target contains all same key value pairs, otherwise false.
     */
    @Override
    public <T extends Map<K, V>, S extends Set<K>, K, V> boolean containsAll(Supplier<T> getter, S source) {
        boolean retValue = false;
        if (getter != null) {
            retValue = collectionUtils.containsAll(getter.get(), source);
        }
        return retValue;
    }

    /**
     * Remove value from collection member provided by getter and return true in case when value was removed.
     *
     * @param getter supplier providing access to collection member in bean.
     * @param value  value to be removed from collection.
     * @return true in case when value was removed from collection, otherwise false
     */
    @Override
    public <C extends Collection<V>, V> boolean remove(Supplier<C> getter, V value) {
        boolean retValue = false;
        if (getter != null) {
            retValue = collectionUtils.remove(getter.get(), value);
        }
        return retValue;
    }

    /**
     * Remove key and value map entry from map member provided by getter and return true in case when value was
     * removed.
     *
     * @param getter supplier providing access to map member in bean.
     * @param key    key part of entry to be removed from map.
     * @param value  value part of entry to be removed from map.
     * @return true in case when key and value was removed from map, otherwise false
     */
    @Override
    public <M extends Map<K, V>, K, V> boolean remove(Supplier<M> getter, K key, V value) {
        boolean retValue = false;
        if (getter != null) {
            retValue = collectionUtils.remove(getter.get(), key, value);
        }
        return retValue;
    }

    /**
     * Remove map entry from map member provided by getter and return true in case when value was removed.
     *
     * @param getter supplier providing access to map member in bean.
     * @param key    key for entry to be removed from map.
     * @return true in case when key and value was removed from map, otherwise false
     */
    @Override
    public <M extends Map<K, V>, K, V> boolean removeKey(Supplier<M> getter, K key) {
        boolean retValue = false;
        if (getter != null) {
            retValue = collectionUtils.remove(getter.get(), key);
        }
        return retValue;
    }

    /**
     * Remove all matching records from collection member provided by getter contained in values source collection.
     *
     * @param getter supplier providing access to map member in bean.
     * @param source values to be removed from collection.
     * @return true in case when collection is modified by provided values, otherwise false.
     */
    @Override
    public <T extends Collection<V>, S extends Collection<V>, V> boolean removeAll(Supplier<T> getter, S source) {
        boolean retValue = false;
        if (getter != null) {
            retValue = collectionUtils.removeAll(getter.get(), source);
        }
        return retValue;
    }

    /**
     * Remove all matching entries from map member provided by getter located in source map.
     *
     * @param getter supplier providing access to map member in bean.
     * @param source map with key pairs to be removed from map.
     * @return m true when map is modified by removal, otherwise false.
     */
    @Override
    public <T extends Map<K, V>, S extends Map<K, V>, K, V> boolean removeAll(Supplier<T> getter, S source) {
        boolean retValue = false;
        if (getter != null) {
            retValue = collectionUtils.removeAll(getter.get(), source);
        }
        return retValue;
    }

    /**
     * Remove all matching keys from target map member provided by getter located in source map.
     *
     * @param getter supplier providing access to map member in bean.
     * @param source map with key pairs to be removed from map.
     * @return m true when map is modified by removal, otherwise false.
     */
    @Override
    public <T extends Map<K, V>, S extends Set<K>, K, V> boolean removeAll(Supplier<T> getter, S source) {
        boolean retValue = false;
        if (getter != null) {
            retValue = collectionUtils.removeAll(getter.get(), source);
        }
        return retValue;
    }

    /**
     * Retain all values existing in collection member provided by getter and provided values.
     *
     * @param getter supplier providing access to map member in bean.
     * @param source values to retain in original collection.
     * @return true if target collection was modified
     */
    @Override
    public <T extends Collection<V>, S extends Collection<V>, V> boolean retainAll(Supplier<T> getter, S source) {
        boolean retValue = false;
        if (getter != null) {
            retValue = collectionUtils.retainAll(getter.get(), source);
        }
        return retValue;
    }

    /**
     * Initialize collection inside bean if needed to prepare add operation. When getter provides null instance init
     * supplier is used to create new instance. If setter consumer is not null new instance is propagated into bean by
     * using this consumer.
     *
     * @param getter supplier of access to bean collection member.
     * @param setter consumer of initialized value to set bean collection member value.
     * @param init   supplier providing new instance of collection in case when it is needed.
     * @param <C>    type of collection.
     * @param <V>    type of value in collection.
     * @return provided collection by getter supplier if is not null otherwise initialized collection by init supplier.
     */
    protected <C extends Collection<V>, V> C initIfNeeded(Supplier<C> getter, Consumer<C> setter, Supplier<C> init) {
        C retValue = null;
        if (getter != null) {
            retValue = getter.get();
            if (retValue == null && init != null) {
                retValue = init.get();
                if (retValue != null && setter != null) {
                    setter.accept(retValue);
                }
            }
        }
        return retValue;
    }

    /**
     * Initialize map inside bean if needed to prepare add operation. When getter provides null instance init supplier
     * is used to create new instance. If setter consumer is not null new instance is propagated into bean by using this
     * consumer.
     *
     * @param getter supplier of access to bean map member.
     * @param setter consumer of initialized value to set bean map member value.
     * @param init   supplier providing new instance of map in case when it is needed.
     * @param <M>    type of map.
     * @param <K>    type of key in map.
     * @param <V>    type of value in map.
     * @return provided map by getter supplier if is not null otherwise initialized map by init supplier.
     */
    protected <M extends Map<K, V>, K, V> M initMapIfNeeded(Supplier<M> getter, Consumer<M> setter, Supplier<M> init) {
        M retValue = null;
        if (getter != null) {
            retValue = getter.get();
            if (retValue == null && init != null) {
                retValue = init.get();
                if (retValue != null && setter != null) {
                    setter.accept(retValue);
                }
            }
        }
        return retValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultBeanCollectionUtils utils = (DefaultBeanCollectionUtils) o;
        return Objects.equals(collectionUtils, utils.collectionUtils);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(collectionUtils);
    }

    @Override
    public String toString() {
        return "DefaultBeanCollectionUtils{" +
                "collectionUtils=" + collectionUtils +
                '}';
    }
}
