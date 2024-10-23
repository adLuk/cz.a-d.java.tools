package cz.ad.java.tools.collection;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CollectionUtilTest {

    @Test
    void isEmpty() {
        Collection<Integer> collection = null;
        boolean result = CollectionUtil.isEmpty(collection);
        assertTrue(result);
        collection = new ArrayList<>();
        result = CollectionUtil.isEmpty(collection);
        assertTrue(result);
        collection.add(1);
        result = CollectionUtil.isEmpty(collection);
        assertFalse(result);

        Map<Integer, Integer> map = null;
        result = CollectionUtil.isEmpty(map);
        assertTrue(result);
        map = new HashMap<>();
        result = CollectionUtil.isEmpty(map);
        assertTrue(result);
        map.put(1, 1);
        result = CollectionUtil.isEmpty(map);
        assertFalse(result);

        Integer[] array = null;
        result = CollectionUtil.isEmpty(array);
        assertTrue(result);
        array = new Integer[]{};
        result = CollectionUtil.isEmpty(array);
        assertTrue(result);

        array = new Integer[]{1};
        result = CollectionUtil.isEmpty(array);
        assertFalse(result);
    }

    @Test
    void isNotEmpty() {
        Collection<Integer> collection = null;
        boolean result = CollectionUtil.isNotEmpty(collection);
        assertFalse(result);
        collection = new ArrayList<>();
        result = CollectionUtil.isNotEmpty(collection);
        assertFalse(result);
        collection.add(1);
        result = CollectionUtil.isNotEmpty(collection);
        assertTrue(result);

        Map<Integer, Integer> map = null;
        result = CollectionUtil.isNotEmpty(map);
        assertFalse(result);
        map = new HashMap<>();
        result = CollectionUtil.isNotEmpty(map);
        assertFalse(result);
        map.put(1, 1);
        result = CollectionUtil.isNotEmpty(map);
        assertTrue(result);

        Integer[] array = null;
        result = CollectionUtil.isNotEmpty(array);
        assertFalse(result);
        array = new Integer[]{};
        result = CollectionUtil.isNotEmpty(array);
        assertFalse(result);
        array = new Integer[]{1};
        result = CollectionUtil.isNotEmpty(array);
        assertTrue(result);
    }

    @Test
    void addList() {
        List<Integer> result = CollectionUtil.addList(null, null);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertNull(result.getFirst());

        result = CollectionUtil.addList(result, 1);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        assertNull(result.getFirst());
        assertEquals(1, result.get(1));
    }

    @Test
    void addSet() {
        Set<Integer> result = CollectionUtil.addSet(null, null);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertNull(result.iterator().next());

        result = CollectionUtil.addSet(result, 1);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        Iterator<Integer> iterator = result.iterator();
        assertNull(iterator.next());
        assertEquals(1, iterator.next());
    }

    @Test
    void add() {
        Collection<Integer> result = CollectionUtil.add(null, ArrayList::new, null);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertNull(result.iterator().next());
    }

    @Test
    void addAllList() {
        List<Integer> result = CollectionUtil.addAllList(null, null);
        assertNull(result);

        List<Integer> list = new ArrayList<>();
        result = CollectionUtil.addAllList(result, list);
        assertNull(result);

        list.add(1);
        result = CollectionUtil.addAllList(result, list);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(1, list.getFirst());
    }

    @Test
    void addAllSet() {
        Set<Integer> result = CollectionUtil.addAllSet(null, null);
        assertNull(result);
        Set<Integer> set = new HashSet<>();
        result = CollectionUtil.addAllSet(result, set);
        assertNull(result);

        set.add(1);
        result = CollectionUtil.addAllSet(result, set);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(1, result.iterator().next());
    }

    @Test
    void addAll() {
        Collection<Integer> result = CollectionUtil.addAll(null, ArrayList::new, null);
        assertNull(result);
        List<Integer> list = new ArrayList<>();
        result = CollectionUtil.addAll(result, ArrayList::new, list);
        assertNull(result);

        list.add(1);
        result = CollectionUtil.addAll(result, ArrayList::new, list);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(1, result.iterator().next());
    }

    @Test
    void putMap() {
        Map<Integer, Integer> result = CollectionUtil.putMap(null, null, null);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertTrue(result.containsKey(null));
        assertNull(result.get(null));
    }

    @Test
    void put() {
        Map<Integer, Integer> result = CollectionUtil.put(null, HashMap::new, null, null);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertTrue(result.containsKey(null));
        assertNull(result.get(null));
    }

    @Test
    void putAll() {
        Map<Integer, Integer> result = CollectionUtil.putAll(null, HashMap::new, null);
        assertNull(result);
        Map<Integer, Integer> map = new HashMap<>();
        result = CollectionUtil.putAll(result, HashMap::new, map);
        assertNull(result);

        map.put(1, 1);
        result = CollectionUtil.putAll(result, HashMap::new, map);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertTrue(result.containsKey(1));
        assertEquals(1, result.get(1));
    }

    @Test
    void putAllMap() {
        Map<Integer, Integer> result = CollectionUtil.putAllMap(null, null);
        assertNull(result);

        Map<Integer, Integer> map = new HashMap<>();
        result = CollectionUtil.putAllMap(result, map);
        assertNull(result);

        map.put(1, 1);
        result = CollectionUtil.putAllMap(result, map);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertTrue(result.containsKey(1));
        assertEquals(1, result.get(1));
    }

    @Test
    void contains() {
        Collection<Integer> collection = null;
        boolean contains = CollectionUtil.contains(collection, null);
        assertFalse(contains);
        collection = new ArrayList<>();
        contains = CollectionUtil.contains(collection, null);
        assertFalse(contains);

        collection.add(1);
        contains = CollectionUtil.contains(collection, null);
        assertFalse(contains);

        contains = CollectionUtil.contains(collection, 1);
        assertTrue(contains);

        Map<Integer, Integer> map = null;
        contains = CollectionUtil.contains(map, null);
        assertFalse(contains);
        map = new HashMap<>();
        contains = CollectionUtil.contains(map, null);
        assertFalse(contains);

        map.put(1, 1);
        contains = CollectionUtil.contains(map, null);
        assertFalse(contains);
        contains = CollectionUtil.contains(map, 1);
        assertTrue(contains);
    }

    @Test
    void containsValue() {
        Map<Integer, Integer> map = null;
        boolean contains = CollectionUtil.containsValue(map, null);
        assertFalse(contains);

        map = new HashMap<>();
        contains = CollectionUtil.containsValue(map, null);
        assertFalse(contains);

        map.put(1, 2);
        contains = CollectionUtil.containsValue(map, 1);
        assertFalse(contains);

        contains = CollectionUtil.containsValue(map, 2);
        assertTrue(contains);
    }

    @Test
    void containsAll() {
        Collection<Integer> collection = null;
        boolean contains = CollectionUtil.containsAll(collection, null);
        assertFalse(contains);
        collection = new ArrayList<>();
        contains = CollectionUtil.containsAll(collection, null);
        assertFalse(contains);
        collection.add(1);
        contains = CollectionUtil.containsAll(collection, collection);
        assertTrue(contains);

        Map<Integer, Integer> map = null;
        contains = CollectionUtil.containsAll(map, map);
        assertFalse(contains);
        map = new HashMap<>();
        contains = CollectionUtil.containsAll(map, map);
        assertFalse(contains);
        map.put(1, 1);
        contains = CollectionUtil.containsAll(map, map);
        assertTrue(contains);

        Set<Integer> set = null;
        map = null;
        contains = CollectionUtil.containsAll(map, set);
        assertFalse(contains);
        map = new HashMap<>();
        set = new HashSet<>();
        contains = CollectionUtil.containsAll(map, set);
        assertFalse(contains);
        map.put(1, 1);
        set.add(1);
        contains = CollectionUtil.containsAll(map, set);
        assertTrue(contains);
    }

    @Test
    void remove() {
        Collection<Integer> collection = null;
        boolean result = CollectionUtil.remove(collection, null);
        assertFalse(result);
        collection = new ArrayList<>();
        result = CollectionUtil.remove(collection, null);
        assertFalse(result);
        collection.add(1);
        result = CollectionUtil.remove(collection, null);
        assertFalse(result);
        result = CollectionUtil.remove(collection, 1);
        assertTrue(result);

        Map<Integer, Integer> map = null;
        result = CollectionUtil.remove(map, null);
        assertFalse(result);
        map = new HashMap<>();
        result = CollectionUtil.remove(map, null);
        assertFalse(result);
        map.put(1, 1);
        result = CollectionUtil.remove(map, null);
        assertFalse(result);
        result = CollectionUtil.remove(map, 1);
        assertTrue(result);

        map = null;
        result = CollectionUtil.remove(map, null, null);
        assertFalse(result);
        map = new HashMap<>();
        result = CollectionUtil.remove(map, null, null);
        assertFalse(result);
        map.put(1, 1);
        result = CollectionUtil.remove(map, null, null);
        assertFalse(result);
        result = CollectionUtil.remove(map, 1, 1);
        assertTrue(result);
    }

    @Test
    void removeAll() {
        Collection<Integer> collection = null;
        boolean result = CollectionUtil.removeAll(collection, null);
        assertFalse(result);
        collection = new ArrayList<>();
        result = CollectionUtil.removeAll(collection, null);
        assertFalse(result);
        collection.add(1);
        result = CollectionUtil.removeAll(collection, null);
        assertFalse(result);
        result = CollectionUtil.removeAll(collection, collection);
        assertTrue(result);

        Map<Integer, Integer> map = null;
        result = CollectionUtil.removeAll(map, map);
        assertFalse(result);
        map = new HashMap<>();
        result = CollectionUtil.removeAll(map, (Map<Integer, Integer>) null);
        assertFalse(result);
        map.put(1, 1);
        result = CollectionUtil.removeAll(map, map);
        assertTrue(result);

        Set<Integer> set = null;
        result = CollectionUtil.removeAll(map, set);
        assertFalse(result);
        map = new HashMap<>();
        set = new HashSet<>();
        result = CollectionUtil.removeAll(map, set);
        assertFalse(result);
        map.put(1, 1);
        set.add(1);
        result = CollectionUtil.removeAll(map, map);
        assertTrue(result);
    }

    @Test
    void retainAll() {
        Collection<Integer> collection = null;
        boolean result = CollectionUtil.retainAll(collection, null);
        assertFalse(result);
        collection = new ArrayList<>();
        result = CollectionUtil.retainAll(collection, null);
        assertFalse(result);
        collection.add(1);
        result = CollectionUtil.retainAll(collection, null);
        assertFalse(result);
        Collection<Integer> source = new ArrayList<>();
        result = CollectionUtil.retainAll(collection, source);
        assertFalse(result);
        source.add(2);
        result = CollectionUtil.retainAll(collection, source);
        assertTrue(result);
    }
}