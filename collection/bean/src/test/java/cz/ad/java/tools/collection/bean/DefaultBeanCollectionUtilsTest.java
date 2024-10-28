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

import cz.ad.java.tools.collection.core.CollectionAddUtils;
import cz.ad.java.tools.collection.core.CollectionUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefaultBeanCollectionUtilsTest {

    @Test
    void constructor() {
        assertThrows(IllegalArgumentException.class, () -> new DefaultBeanCollectionUtils(null));
        CollectionAddUtils defaultInstance = CollectionUtils.DEFAULT_INSTANCE;
        DefaultBeanCollectionUtils utils = new DefaultBeanCollectionUtils(defaultInstance);
        assertSame(defaultInstance, utils.collectionUtils);

        utils = new DefaultBeanCollectionUtils();
        assertNotSame(defaultInstance, utils.collectionUtils);
    }

    @Test
    void isEmpty() {
        DefaultBeanCollectionUtils testesInstance = testesInstance();
        boolean empty = testesInstance.isEmpty(null);
        assertTrue(empty);
        TestBean testBean = new TestBean();
        empty = testesInstance.isEmpty(testBean::getList);
        assertTrue(empty);
        ArrayList<Integer> list = new ArrayList<>();
        testBean.setList(list);
        empty = testesInstance.isEmpty(testBean::getList);
        assertTrue(empty);
        list.add(1);
        empty = testesInstance.isEmpty(testBean::getList);
        assertFalse(empty);

        HashSet<Integer> set = new HashSet<>();
        testBean.setSet(set);
        empty = testesInstance.isEmpty(testBean::getSet);
        assertTrue(empty);
        set.add(1);
        empty = testesInstance.isEmpty(testBean::getSet);
        assertFalse(empty);
    }

    @Test
    void isMapEmpty() {
        DefaultBeanCollectionUtils testesInstance = testesInstance();
        boolean empty = testesInstance.isMapEmpty(null);
        assertTrue(empty);
        TestBean testBean = new TestBean();
        empty = testesInstance.isMapEmpty(testBean::getMap);
        assertTrue(empty);
        Map<Integer, Integer> map = new HashMap<>();
        testBean.setMap(map);
        empty = testesInstance.isMapEmpty(testBean::getMap);
        assertTrue(empty);
        map.put(1, 1);
        empty = testesInstance.isMapEmpty(testBean::getMap);
        assertFalse(empty);
    }

    @Test
    void isArrayEmpty() {
        DefaultBeanCollectionUtils testesInstance = testesInstance();
        boolean empty = testesInstance.isArrayEmpty(null);
        assertTrue(empty);
        TestBean testBean = new TestBean();
        Integer[] array = null;
        testBean.setArray(array);
        empty = testesInstance.isArrayEmpty(testBean::getArray);
        assertTrue(empty);
        array = new Integer[]{1, 2};
        testBean.setArray(array);
        empty = testesInstance.isArrayEmpty(testBean::getArray);
        assertFalse(empty);
    }

    @Test
    void isNotEmpty() {
        DefaultBeanCollectionUtils testesInstance = testesInstance();
        boolean empty = testesInstance.isNotEmpty(null);
        assertFalse(empty);
        TestBean testBean = new TestBean();
        empty = testesInstance.isNotEmpty(testBean::getList);
        assertFalse(empty);
        List<Integer> list = new ArrayList<>();
        testBean.setList(list);
        empty = testesInstance.isNotEmpty(testBean::getList);
        assertFalse(empty);
        list.add(1);
        empty = testesInstance.isNotEmpty(testBean::getList);
        assertTrue(empty);

        HashSet<Integer> set = new HashSet<>();
        testBean.setSet(set);
        empty = testesInstance.isNotEmpty(testBean::getSet);
        assertFalse(empty);
        set.add(1);
        empty = testesInstance.isNotEmpty(testBean::getSet);
        assertTrue(empty);
    }

    @Test
    void isMapNotEmpty() {
        DefaultBeanCollectionUtils testesInstance = testesInstance();
        boolean empty = testesInstance.isMapNotEmpty(null);
        assertFalse(empty);

        TestBean testBean = new TestBean();
        empty = testesInstance.isMapNotEmpty(testBean::getMap);
        assertFalse(empty);
        Map<Integer, Integer> map = new HashMap<>();
        testBean.setMap(map);
        empty = testesInstance.isMapNotEmpty(testBean::getMap);
        assertFalse(empty);
        map.put(1, 1);
        empty = testesInstance.isMapNotEmpty(testBean::getMap);
        assertTrue(empty);
    }

    @Test
    void isArrayNotEmpty() {
        DefaultBeanCollectionUtils testesInstance = testesInstance();
        boolean empty = testesInstance.isArrayNotEmpty(null);
        assertFalse(empty);
        TestBean testBean = new TestBean();
        empty = testesInstance.isArrayNotEmpty(testBean::getArray);
        assertFalse(empty);
        Integer[] array = null;
        testBean.setArray(array);
        empty = testesInstance.isArrayNotEmpty(testBean::getArray);
        assertFalse(empty);
        array = new Integer[]{1, 2};
        testBean.setArray(array);
        empty = testesInstance.isArrayNotEmpty(testBean::getArray);
        assertTrue(empty);
    }

    @Test
    void add() {
        DefaultBeanCollectionUtils testesInstance = testesInstance();
        Collection<Object> result = testesInstance.add(null, null, null, null);
        assertNull(result);
        TestBean testBean = new TestBean();
        List<Integer> add = testesInstance.add(testBean::getList, testBean::setList, ArrayList::new, null);
        assertNotNull(add);
        assertSame(testBean.getList(), add);
        assertFalse(add.isEmpty());
        assertEquals(1, testBean.getList().size());
        assertNull(testBean.getList().getFirst());
    }

    @Test
    void addAll() {
        DefaultBeanCollectionUtils testesInstance = testesInstance();
        Collection<Object> result = testesInstance.addAll(null, null, null, null);
        assertNull(result);
        TestBean testBean = new TestBean();
        List<Integer> add = testesInstance.addAll(testBean::getList, testBean::setList, ArrayList::new, null);
        assertNull(add);
        List<Integer> list = new ArrayList<>();
        add = testesInstance.addAll(testBean::getList, testBean::setList, ArrayList::new, list);
        assertNull(add);
        list.add(1);
        add = testesInstance.addAll(testBean::getList, testBean::setList, ArrayList::new, list);
        assertNotNull(add);
        assertSame(testBean.getList(), add);
        assertFalse(add.isEmpty());
        assertEquals(1, add.size());
        assertEquals(1, add.getFirst());
    }

    @Test
    void put() {
        DefaultBeanCollectionUtils testesInstance = testesInstance();
        Map<Integer, Integer> put = testesInstance.put(null, null, null, null, null);
        assertNull(put);
        TestBean testBean = new TestBean();
        put = testesInstance.put(testBean::getMap, testBean::setMap, HashMap::new, null, null);
        assertNotNull(put);
        assertSame(testBean.getMap(), put);
        assertFalse(put.isEmpty());
        assertEquals(1, put.size());
        assertTrue(put.containsKey(null));
        assertNull(put.get(null));
    }

    @Test
    void putAll() {
        DefaultBeanCollectionUtils testesInstance = testesInstance();
        Map<Integer, Integer> put = testesInstance.putAll(null, null, null, null);
        assertNull(put);
        TestBean testBean = new TestBean();
        put = testesInstance.putAll(testBean::getMap, testBean::setMap, HashMap::new, null);
        assertNull(put);
        Map<Integer, Integer> map = new HashMap<>();
        put = testesInstance.putAll(testBean::getMap, testBean::setMap, HashMap::new, map);
        assertNull(put);

        map.put(1, 1);
        put = testesInstance.putAll(testBean::getMap, testBean::setMap, HashMap::new, map);
        assertSame(testBean.getMap(), put);
        assertFalse(put.isEmpty());
        assertEquals(1, put.size());
        assertTrue(put.containsKey(1));
        assertEquals(1, put.get(1));
    }

    @Test
    void contains() {
        DefaultBeanCollectionUtils testesInstance = testesInstance();
        boolean contains = testesInstance.contains(null, null);
        assertFalse(contains);
        TestBean testBean = new TestBean();
        contains = testesInstance.contains(testBean::getList, null);
        assertFalse(contains);
        List<Integer> list = new ArrayList<>();
        testBean.setList(list);
        contains = testesInstance.contains(testBean::getList, null);
        assertFalse(contains);
        list.add(1);
        contains = testesInstance.contains(testBean::getList, 1);
        assertTrue(contains);
    }

    @Test
    void containsKey() {
        DefaultBeanCollectionUtils testesInstance = testesInstance();
        boolean contains = testesInstance.containsKey(null, null);
        assertFalse(contains);
        TestBean testBean = new TestBean();
        contains = testesInstance.containsKey(testBean::getMap, null);
        assertFalse(contains);
        Map<Integer, Integer> map = new HashMap<>();
        testBean.setMap(map);
        contains = testesInstance.containsKey(testBean::getMap, null);
        assertFalse(contains);
        map.put(1, 1);
        contains = testesInstance.containsKey(testBean::getMap, 1);
        assertTrue(contains);
    }

    @Test
    void containsValue() {
        DefaultBeanCollectionUtils testesInstance = testesInstance();
        boolean contains = testesInstance.containsValue(null, null);
        assertFalse(contains);
        TestBean testBean = new TestBean();
        contains = testesInstance.containsValue(testBean::getMap, null);
        assertFalse(contains);
        Map<Integer, Integer> map = new HashMap<>();
        testBean.setMap(map);
        contains = testesInstance.containsValue(testBean::getMap, null);
        assertFalse(contains);
        map.put(1, 2);
        contains = testesInstance.containsValue(testBean::getMap, 2);
        assertTrue(contains);
    }

    @Test
    void containsAll() {
        DefaultBeanCollectionUtils testesInstance = testesInstance();
        List<Integer> source = null;
        boolean containsAll = testesInstance.containsAll(null, source);
        assertFalse(containsAll);
        TestBean testBean = new TestBean();
        containsAll = testesInstance.containsAll(testBean::getList, source);
        assertFalse(containsAll);
        source = new ArrayList<>();
        containsAll = testesInstance.containsAll(testBean::getList, source);
        assertFalse(containsAll);
        List<Integer> list = new ArrayList<>();
        testBean.setList(list);
        containsAll = testesInstance.containsAll(testBean::getList, source);
        assertFalse(containsAll);
        list.add(1);
        containsAll = testesInstance.containsAll(testBean::getList, source);
        assertFalse(containsAll);
        source.add(1);
        containsAll = testesInstance.containsAll(testBean::getList, source);
        assertTrue(containsAll);
    }

    @Test
    void testContainsAll() {
        DefaultBeanCollectionUtils testesInstance = testesInstance();
        Set<Integer> source = null;
        boolean containsAll = testesInstance.containsAll((Supplier<Collection<Integer>>) null, source);
        assertFalse(containsAll);
        TestBean testBean = new TestBean();
        containsAll = testesInstance.containsAll(testBean::getSet, source);
        assertFalse(containsAll);
        source = new HashSet<>();
        containsAll = testesInstance.containsAll(testBean::getSet, source);
        assertFalse(containsAll);
        Set<Integer> list = new HashSet<>();
        testBean.setSet(list);
        containsAll = testesInstance.containsAll(testBean::getSet, source);
        assertFalse(containsAll);
        list.add(1);
        containsAll = testesInstance.containsAll(testBean::getSet, source);
        assertFalse(containsAll);
        source.add(1);
        containsAll = testesInstance.containsAll(testBean::getSet, source);
        assertTrue(containsAll);
    }

    @Test
    void testContainsAll1() {
        DefaultBeanCollectionUtils testesInstance = testesInstance();
        Map<Integer, Integer> source = null;
        boolean containsAll = testesInstance.containsAll(null, source);
        assertFalse(containsAll);
        TestBean testBean = new TestBean();
        containsAll = testesInstance.containsAll(testBean::getMap, source);
        assertFalse(containsAll);
        source = new HashMap<>();
        containsAll = testesInstance.containsAll(testBean::getMap, source);
        assertFalse(containsAll);
        Map<Integer, Integer> map = new HashMap<>();
        testBean.setMap(map);
        containsAll = testesInstance.containsAll(testBean::getMap, source);
        assertFalse(containsAll);
        map.put(1, 2);
        containsAll = testesInstance.containsAll(testBean::getMap, source);
        assertFalse(containsAll);
        source.put(1, 2);
        containsAll = testesInstance.containsAll(testBean::getMap, source);
        assertTrue(containsAll);

        Set<Integer> set = new HashSet<>();
        map.clear();

        containsAll = testesInstance.containsAll((Supplier<Map<Integer, Integer>>) null, set);
        assertFalse(containsAll);

        containsAll = testesInstance.containsAll(testBean::getMap, set);
        assertFalse(containsAll);
        set.add(1);
        containsAll = testesInstance.containsAll(testBean::getMap, set);
        assertFalse(containsAll);
        map.put(1, 2);
        containsAll = testesInstance.containsAll(testBean::getMap, set);
        assertTrue(containsAll);
    }

    @Test
    void remove() {
        DefaultBeanCollectionUtils testesInstance = testesInstance();
        boolean remove = testesInstance.remove(null, null);
        assertFalse(remove);
        TestBean testBean = new TestBean();
        remove = testesInstance.remove(testBean::getList, null);
        assertFalse(remove);
        List<Integer> list = new ArrayList<>();
        testBean.setList(list);
        remove = testesInstance.remove(testBean::getList, null);
        assertFalse(remove);
        list.add(null);
        remove = testesInstance.remove(testBean::getList, null);
        assertTrue(remove);
    }

    @Test
    void testRemove() {
        DefaultBeanCollectionUtils testesInstance = testesInstance();
        boolean remove = testesInstance.remove(null, null);
        assertFalse(remove);
        TestBean testBean = new TestBean();
        remove = testesInstance.remove(testBean::getMap, null, null);
        assertFalse(remove);
        Map<Integer, Integer> map = new HashMap<>();
        testBean.setMap(map);
        remove = testesInstance.remove(testBean::getMap, null, null);
        assertFalse(remove);
        map.put(1, 2);
        remove = testesInstance.remove(testBean::getMap, null, null);
        assertFalse(remove);
        remove = testesInstance.remove(testBean::getMap, 1, 2);
        assertTrue(remove);
    }

    @Test
    void removeKey() {
        DefaultBeanCollectionUtils testesInstance = testesInstance();
        boolean remove = testesInstance.removeKey(null, null);
        assertFalse(remove);
        TestBean testBean = new TestBean();
        remove = testesInstance.removeKey(testBean::getMap, null);
        assertFalse(remove);
        Map<Integer, Integer> map = new HashMap<>();
        testBean.setMap(map);
        remove = testesInstance.removeKey(testBean::getMap, null);
        assertFalse(remove);
        map.put(1, 2);
        remove = testesInstance.removeKey(testBean::getMap, null);
        assertFalse(remove);
        remove = testesInstance.removeKey(testBean::getMap, 1);
        assertTrue(remove);
    }

    @Test
    void removeAll() {
        DefaultBeanCollectionUtils testesInstance = testesInstance();
        List<Integer> source = null;
        boolean removedAll = testesInstance.removeAll(null, source);
        assertFalse(removedAll);
        TestBean testBean = new TestBean();
        removedAll = testesInstance.removeAll(testBean::getList, source);
        assertFalse(removedAll);
        List<Integer> list = new ArrayList<>();
        testBean.setList(list);
        removedAll = testesInstance.removeAll(testBean::getList, source);
        assertFalse(removedAll);
        source = new ArrayList<>();
        removedAll = testesInstance.removeAll(testBean::getList, source);
        assertFalse(removedAll);

        list.add(1);
        removedAll = testesInstance.removeAll(testBean::getList, source);
        assertFalse(removedAll);
        source.add(1);
        removedAll = testesInstance.removeAll(testBean::getList, source);
        assertTrue(removedAll);
    }

    @Test
    void testRemoveAll() {
        DefaultBeanCollectionUtils testesInstance = testesInstance();
        Map<Integer, Integer> source = null;
        boolean removedAll = testesInstance.removeAll(null, source);
        assertFalse(removedAll);
        TestBean testBean = new TestBean();
        removedAll = testesInstance.removeAll(testBean::getMap, source);
        assertFalse(removedAll);
        Map<Integer, Integer> list = new HashMap<>();
        testBean.setMap(list);
        removedAll = testesInstance.removeAll(testBean::getMap, source);
        assertFalse(removedAll);
        source = new HashMap<>();
        removedAll = testesInstance.removeAll(testBean::getMap, source);
        assertFalse(removedAll);

        list.put(1, 2);
        removedAll = testesInstance.removeAll(testBean::getMap, source);
        assertFalse(removedAll);
        source.put(1, 2);
        removedAll = testesInstance.removeAll(testBean::getMap, source);
        assertTrue(removedAll);
    }

    @Test
    void testRemoveAll1() {
        DefaultBeanCollectionUtils testesInstance = testesInstance();
        Set<Integer> source = null;
        boolean removedAll = testesInstance.removeAll((Supplier<Map<Integer, Integer>>) null, source);
        assertFalse(removedAll);
        TestBean testBean = new TestBean();
        removedAll = testesInstance.removeAll(testBean::getMap, source);
        assertFalse(removedAll);
        Map<Integer, Integer> list = new HashMap<>();
        testBean.setMap(list);
        removedAll = testesInstance.removeAll(testBean::getMap, source);
        assertFalse(removedAll);
        source = new HashSet<>();
        removedAll = testesInstance.removeAll(testBean::getMap, source);
        assertFalse(removedAll);

        list.put(1, 2);
        removedAll = testesInstance.removeAll(testBean::getMap, source);
        assertFalse(removedAll);
        source.add(1);
        removedAll = testesInstance.removeAll(testBean::getMap, source);
        assertTrue(removedAll);
    }

    @Test
    void retainAll() {
        DefaultBeanCollectionUtils testesInstance = testesInstance();
        Set<Integer> source = null;
        boolean all = testesInstance.retainAll(null, source);
        assertFalse(all);
        TestBean testBean = new TestBean();
        all = testesInstance.retainAll(testBean::getList, source);
        assertFalse(all);
        List<Integer> list = new ArrayList<>();
        testBean.setList(list);
        all = testesInstance.retainAll(testBean::getList, source);
        assertFalse(all);
        source = new HashSet<>();
        all = testesInstance.retainAll(testBean::getList, source);
        assertFalse(all);
        source.add(1);
        all = testesInstance.retainAll(testBean::getList, source);
        assertFalse(all);
        list.add(2);
        all = testesInstance.retainAll(testBean::getList, source);
        assertTrue(all);
    }

    @Test
    void initIfNeeded() {
        DefaultBeanCollectionUtils testesInstance = testesInstance();
        Collection<Integer> collection = testesInstance.initIfNeeded(null, null, null);
        assertNull(collection);
        TestBean testBean = new TestBean();
        collection = testesInstance.initIfNeeded(testBean::getList, testBean::setList, null);
        assertNull(collection);
        collection = testesInstance.initIfNeeded(testBean::getList, testBean::setList, ArrayList::new);
        assertNotNull(collection);
    }

    @Test
    void initMapIfNeeded() {
        DefaultBeanCollectionUtils testesInstance = testesInstance();
        Map<Integer, Integer> collection = testesInstance.initMapIfNeeded(null, null, null);
        assertNull(collection);
        TestBean testBean = new TestBean();
        collection = testesInstance.initMapIfNeeded(testBean::getMap, testBean::setMap, null);
        assertNull(collection);
        collection = testesInstance.initMapIfNeeded(testBean::getMap, testBean::setMap, HashMap::new);
        assertNotNull(collection);
    }

    protected DefaultBeanCollectionUtils testesInstance() {
        return new DefaultBeanCollectionUtils();
    }

    static class TestBean {
        protected List<Integer> list;

        protected Set<Integer> set;

        protected Map<Integer, Integer> map;

        protected Integer[] array;

        public List<Integer> getList() {
            return list;
        }

        public void setList(List<Integer> list) {
            this.list = list;
        }

        public Set<Integer> getSet() {
            return set;
        }

        public void setSet(Set<Integer> set) {
            this.set = set;
        }

        public Map<Integer, Integer> getMap() {
            return map;
        }

        public void setMap(Map<Integer, Integer> map) {
            this.map = map;
        }

        public Integer[] getArray() {
            return array;
        }

        public void setArray(Integer[] array) {
            this.array = array;
        }
    }
}