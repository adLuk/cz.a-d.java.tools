package cz.ad.java.tools.collection;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefaultCollectionUtilsTest {

    @Test
    void isEmpty() {
        DefaultCollectionUtils instance = testedInstance();

        Collection<Integer> collection = null;
        boolean result = instance.isEmpty(collection);
        assertTrue(result);
        collection = Collections.emptyList();
        result = instance.isEmpty(collection);
        assertTrue(result);

        collection = List.of(1, 2, 3);
        result = instance.isEmpty(collection);
        assertFalse(result);

        Map<Integer, Integer> map = null;
        result = instance.isEmpty(map);
        assertTrue(result);
        map = Collections.emptyMap();
        result = instance.isEmpty(map);
        assertTrue(result);
        map = Map.of(1, 2, 3, 4);
        result = instance.isEmpty(map);
        assertFalse(result);

        Integer[] array = null;
        result = instance.isEmpty(array);
        assertTrue(result);
        array = new Integer[]{};
        result = instance.isEmpty(array);
        assertTrue(result);

        array = new Integer[]{1, 2, 3};
        result = instance.isEmpty(array);
        assertFalse(result);
    }

    @Test
    void isNotEmpty() {
        DefaultCollectionUtils instance = testedInstance();
        Collection<Integer> collection = null;
        boolean result = instance.isNotEmpty(collection);
        assertFalse(result);
        collection = Collections.emptyList();
        result = instance.isNotEmpty(collection);
        assertFalse(result);
        collection = List.of(1, 2, 3);
        result = instance.isNotEmpty(collection);
        assertTrue(result);

        Map<Integer, Integer> map = null;
        result = instance.isNotEmpty(map);
        assertFalse(result);
        map = Collections.emptyMap();
        result = instance.isNotEmpty(map);
        assertFalse(result);
        map = Map.of(1, 2, 3, 4);
        result = instance.isNotEmpty(map);
        assertTrue(result);

        Integer[] array = null;
        result = instance.isNotEmpty(array);
        assertFalse(result);
        array = new Integer[]{};
        result = instance.isNotEmpty(array);
        assertFalse(result);
        array = new Integer[]{1, 2, 3};
        result = instance.isNotEmpty(array);
        assertTrue(result);
    }

    @Test
    void add() {
        DefaultCollectionUtils instance = testedInstance();
        List<Integer> result = instance.add(null, ArrayList::new, null);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertNull(result.getFirst());

        result = instance.add(null, () -> null, null);
        assertNull(result);
        result = instance.add(result, ArrayList::new, 1);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.getFirst());

        result = instance.add(result, ArrayList::new, 2);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1, result.getFirst());
        assertEquals(2, result.get(1));
    }

    @Test
    void addAll() {
        DefaultCollectionUtils instance = testedInstance();
        List<Integer> result = instance.addAll(null, ArrayList::new, null);
        assertNull(result);

        List<Integer> list = new ArrayList<>();
        result = instance.addAll(null, ArrayList::new, list);
        assertNull(result);

        list.add(1);
        result = instance.addAll(result, ArrayList::new, list);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.getFirst());

        result = instance.addAll(null, () -> null, list);
        assertNull(result);

        list.add(2);
        result = instance.addAll(result, ArrayList::new, list);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1, result.getFirst());
        assertEquals(2, result.get(1));

        result = instance.addAll(result, ArrayList::new, list);
        assertNotNull(result);
        assertEquals(4, result.size());
        assertEquals(1, result.getFirst());
        assertEquals(2, result.get(1));
        assertEquals(1, result.get(2));
        assertEquals(2, result.get(3));
    }

    @Test
    void put() {
        DefaultCollectionUtils instance = testedInstance();

        HashMap<Integer, Integer> map = instance.put(null, HashMap::new, null, null);
        assertNotNull(map);
        assertEquals(1, map.size());
        assertTrue(map.containsKey(null));
        assertNull(map.get(null));

        map = instance.put(null, () -> null, null, null);
        assertNull(map);

        map = instance.put(new HashMap<>(), HashMap::new, null, null);
        assertNotNull(map);
        assertEquals(1, map.size());
        assertTrue(map.containsKey(null));
        assertNull(map.get(null));
    }

    @Test
    void putAll() {
        DefaultCollectionUtils instance = testedInstance();

        HashMap<Integer, Integer> map = instance.putAll(null, HashMap::new, null);
        assertNull(map);

        Map<Integer, Integer> source = new HashMap<>();
        map = instance.putAll(map, HashMap::new, source);
        assertNull(map);

        source.put(1, 2);
        map = instance.putAll(map, () -> null, source);
        assertNull(map);

        map = instance.putAll(map, HashMap::new, source);
        assertNotNull(map);
        assertEquals(1, map.size());
        assertTrue(map.containsKey(1));
        assertEquals(2, map.get(1));

        map = instance.putAll(map, HashMap::new, source);
        assertNotNull(map);
        assertEquals(1, map.size());
        assertTrue(map.containsKey(1));
        assertEquals(2, map.get(1));
    }

    @Test
    void contains() {
        DefaultCollectionUtils instance = testedInstance();

        Collection<Integer> collection = null;
        boolean result = instance.contains(collection, null);
        assertFalse(result);

        collection = Collections.emptyList();
        result = instance.contains(collection, null);
        assertFalse(result);

        collection = List.of(1, 2, 3);
        result = instance.contains(collection, 1);
        assertTrue(result);

        Map<Integer, Integer> map = null;
        result = instance.contains(map, null);
        assertFalse(result);
        map = Collections.emptyMap();
        result = instance.contains(map, null);
        assertFalse(result);
        map = Map.of(1, 2, 3, 4);
        result = instance.contains(map, 1);
        assertTrue(result);
    }

    @Test
    void containsValue() {
        DefaultCollectionUtils instance = testedInstance();

        Map<Integer, Integer> map = null;
        boolean result = instance.containsValue(map, null);
        assertFalse(result);

        map = Collections.emptyMap();
        result = instance.containsValue(map, null);
        assertFalse(result);

        map = Map.of(1, 2, 3, 4);
        result = instance.containsValue(map, 2);
        assertTrue(result);
    }

    @Test
    void containsAll() {
        DefaultCollectionUtils instance = testedInstance();

        Collection<Integer> collection = null;
        boolean result = instance.containsAll(collection, null);
        assertFalse(result);
        collection = Collections.emptyList();
        result = instance.containsAll(collection, null);
        assertFalse(result);
        collection = List.of(1, 2, 3);
        Set<Integer> source = Set.of(1, 2, 3);
        result = instance.containsAll(collection, source);
        assertTrue(result);

        Map<Integer, Integer> map = null;
        Map<Integer, Integer> source2 = null;
        result = instance.containsAll(map, source2);
        assertFalse(result);

        map = Collections.emptyMap();
        result = instance.containsAll(map, source2);
        assertFalse(result);

        source2 = Collections.emptyMap();
        map = null;
        result = instance.containsAll(map, source2);
        assertFalse(result);

        map = Map.of(1, 2, 3, 4);
        result = instance.containsAll(map, source2);
        assertFalse(result);

        map = null;
        source2 = Map.of(1, 2, 3, 4);
        result = instance.containsAll(map, source2);
        assertFalse(result);

        map = Map.of(1, 2);
        result = instance.containsAll(map, source2);
        assertFalse(result);

        map = Map.of(1, 3);
        source2 = Map.of(1, 2);
        result = instance.containsAll(map, source2);
        assertFalse(result);

        map = Map.of(1, 2, 3, 4);
        result = instance.containsAll(map, source2);
        assertTrue(result);

        source = null;
        map = null;
        result = instance.containsAll(map, source);
        assertFalse(result);

        source = Set.of(1, 2, 3);
        result = instance.containsAll(map, source);
        assertFalse(result);

        map = Map.of(1, 2, 3, 4);
        source = null;
        result = instance.containsAll(map, source);
        assertFalse(result);

        source = Set.of(1, 2, 3);
        result = instance.containsAll(map, source);
        assertFalse(result);

        source = Set.of(1, 3);
        result = instance.containsAll(map, source);
        assertTrue(result);
    }

    @Test
    void remove() {
        DefaultCollectionUtils instance = testedInstance();

        Collection<Integer> collection = null;
        boolean remove = instance.remove(collection, null);
        assertFalse(remove);
        collection = Collections.emptyList();
        remove = instance.remove(collection, null);
        assertFalse(remove);
        collection = new ArrayList<>(List.of(1, 2, 3));
        remove = instance.remove(collection, null);
        assertFalse(remove);
        remove = instance.remove(collection, 1);
        assertTrue(remove);

        Map<Integer, Integer> map = null;
        remove = instance.remove(map, null, null);
        assertFalse(remove);
        map = new HashMap<>();
        remove = instance.remove(map, null, null);
        assertFalse(remove);

        map = new HashMap<>(Map.of(1, 2, 3, 4));
        remove = instance.remove(map, null, null);
        assertFalse(remove);

        remove = instance.remove(map, 1, null);
        assertFalse(remove);
        remove = instance.remove(map, 1, 2);
        assertTrue(remove);

        map = null;
        remove = instance.remove(map, null);
        assertFalse(remove);

        map = new HashMap<>();
        remove = instance.remove(map, null);
        assertFalse(remove);

        remove = instance.remove(map, 1);
        assertFalse(remove);

        map = new HashMap<>(Map.of(1, 2, 3, 4));
        remove = instance.remove(map, 2);
        assertFalse(remove);

        remove = instance.remove(map, 1);
        assertTrue(remove);
    }

    @Test
    void removeAll() {
        DefaultCollectionUtils instance = testedInstance();
        Collection<Integer> collection = null;
        Collection<Integer> source = null;
        boolean remove = instance.removeAll(collection, source);
        assertFalse(remove);

        collection = List.of(1, 2, 3);
        remove = instance.removeAll(collection, source);
        assertFalse(remove);

        collection = null;
        source = List.of(1, 2, 3);
        remove = instance.removeAll(collection, source);
        assertFalse(remove);

        collection = new ArrayList<>(List.of(1, 2, 3));
        source = List.of(1, 2, 3);
        remove = instance.removeAll(collection, source);
        assertTrue(remove);

        Map<Integer, Integer> map = null;
        Map<Integer, Integer> source2 = null;
        remove = instance.removeAll(map, source2);
        assertFalse(remove);

        map = new HashMap<>();
        remove = instance.removeAll(map, source2);
        assertFalse(remove);

        map = new HashMap<>(Map.of(1, 2, 3, 4));
        remove = instance.removeAll(map, source2);
        assertFalse(remove);
        source2 = new HashMap<>(Map.of(1, 2));
        map = null;
        remove = instance.removeAll(map, source2);
        assertFalse(remove);

        map = new HashMap<>(Map.of(1, 2, 3, 4));
        source2 = new HashMap<>(Map.of(1, 1));
        remove = instance.removeAll(map, source2);
        assertFalse(remove);

        source2 = new HashMap<>(Map.of(1, 2, 3, 4));
        remove = instance.removeAll(map, source2);
        assertTrue(remove);

        Set<Integer> set = null;
        remove = instance.removeAll(map, set);
        assertFalse(remove);

        set = Set.of(1, 2, 3);
        remove = instance.removeAll(map, set);
        assertFalse(remove);

        map = new HashMap<>(Map.of(1, 2, 3, 4));
        remove = instance.removeAll(map, set);
        assertTrue(remove);
    }

    @Test
    void retainAll() {
        DefaultCollectionUtils instance = testedInstance();
        Collection<Integer> collection = null;
        Collection<Integer> source = null;
        boolean retain = instance.retainAll(collection, source);
        assertFalse(retain);
        collection = List.of(1, 2, 3);
        retain = instance.retainAll(collection, source);
        assertFalse(retain);

        collection = null;
        source = new ArrayList<>(List.of(1, 2));
        retain = instance.retainAll(collection, source);
        assertFalse(retain);

        collection = new ArrayList<>(List.of(1, 2, 3));
        retain = instance.retainAll(collection, source);
        assertTrue(retain);
    }

    /**
     * Initialize tested instance for usage by unit tests.
     *
     * @return initialized instance for testing.
     */
    protected DefaultCollectionUtils testedInstance() {
        return new DefaultCollectionUtils();
    }
}