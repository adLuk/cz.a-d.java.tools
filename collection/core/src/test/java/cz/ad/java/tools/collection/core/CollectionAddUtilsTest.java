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

package cz.ad.java.tools.collection.core;

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