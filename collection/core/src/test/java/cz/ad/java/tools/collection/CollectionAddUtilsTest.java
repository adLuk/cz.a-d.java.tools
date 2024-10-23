package cz.ad.java.tools.collection;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CollectionAddUtilsTest {

    @Test
    void addList() {
        List<Object> result = CollectionAddUtils.DEFAULT_INSTANCE.addList(null, null);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertNull(result.getFirst());
    }

    @Test
    void addSet() {
        Set<Object> result = CollectionAddUtils.DEFAULT_INSTANCE.addSet(null, null);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertNull(result.iterator().next());
    }

    @Test
    void addAllList() {
        List<Object> result = CollectionAddUtils.DEFAULT_INSTANCE.addAllList(null, null);
        assertNull(result);
        result = CollectionAddUtils.DEFAULT_INSTANCE.addAllList(null, List.of(1));
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(1, result.getFirst());
    }

    @Test
    void addAllSet() {
        Set<Object> result = CollectionAddUtils.DEFAULT_INSTANCE.addAllSet(null, null);
        assertNull(result);
        result = CollectionAddUtils.DEFAULT_INSTANCE.addAllSet(null, List.of(1));
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(1, result.iterator().next());
    }

    @Test
    void putMap() {
        Integer key = null;
        Integer value = null;
        Map<Integer, Integer> result = CollectionAddUtils.DEFAULT_INSTANCE.putMap(null, key, value);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertTrue(result.containsKey(key));
        assertNull(result.get(key));
    }

    @Test
    void putAllMap() {
        Map<Integer, Integer> result = CollectionAddUtils.DEFAULT_INSTANCE.putAllMap(null, null);
        assertNull(result);

        Map<Integer, Integer> source = Map.of(1, 2, 3, 4);
        result = CollectionAddUtils.DEFAULT_INSTANCE.putAllMap(null, source);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
    }

}