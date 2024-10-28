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

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class BeanCollectionAddUtilsTest {

    @Test
    void addList() {
        BeanCollectionAddUtils instance = testedInstance();
        List<Integer> added = instance.addList(null, null, null);
        assertNull(added);
        DefaultBeanCollectionUtilsTest.TestBean testBean = new DefaultBeanCollectionUtilsTest.TestBean();
        added = instance.addList(testBean::getList, testBean::setList, null);
        assertNotNull(added);
    }

    @Test
    void addSet() {
        BeanCollectionAddUtils instance = testedInstance();
        Set<Integer> added = instance.addSet(null, null, null);
        assertNull(added);
        DefaultBeanCollectionUtilsTest.TestBean testBean = new DefaultBeanCollectionUtilsTest.TestBean();
        added = instance.addSet(testBean::getSet, testBean::setSet, null);
        assertNotNull(added);
    }

    @Test
    void addAllList() {
        BeanCollectionAddUtils instance = testedInstance();
        List<Integer> added = instance.addAllList(null, null, null);
        assertNull(added);
        DefaultBeanCollectionUtilsTest.TestBean testBean = new DefaultBeanCollectionUtilsTest.TestBean();
        added = instance.addAllList(testBean::getList, testBean::setList, null);
        assertNull(added);

        List<Integer> list = new ArrayList<>();
        added = instance.addAllList(testBean::getList, testBean::setList, list);
        assertNull(added);
        list.add(1);
        added = instance.addAllList(testBean::getList, testBean::setList, list);
        assertNotNull(added);
    }

    @Test
    void addAllSet() {
        BeanCollectionAddUtils instance = testedInstance();
        Set<Integer> added = instance.addAllSet(null, null, null);
        assertNull(added);
        DefaultBeanCollectionUtilsTest.TestBean testBean = new DefaultBeanCollectionUtilsTest.TestBean();
        added = instance.addAllSet(testBean::getSet, testBean::setSet, null);
        assertNull(added);

        List<Integer> list = new ArrayList<>();
        added = instance.addAllSet(testBean::getSet, testBean::setSet, list);
        assertNull(added);
        list.add(1);
        added = instance.addAllSet(testBean::getSet, testBean::setSet, list);
        assertNotNull(added);
    }

    @Test
    void putMap() {
        BeanCollectionAddUtils instance = testedInstance();
        Map<Integer, Integer> added = instance.putMap(null, null, null, null);
        assertNull(added);
        DefaultBeanCollectionUtilsTest.TestBean testBean = new DefaultBeanCollectionUtilsTest.TestBean();
        added = instance.putMap(testBean::getMap, testBean::setMap, null, null);
        assertNotNull(added);
    }

    @Test
    void putAllMap() {
        BeanCollectionAddUtils instance = testedInstance();
        Map<Integer, Integer> added = instance.putAllMap(null, null, null);
        assertNull(added);
        DefaultBeanCollectionUtilsTest.TestBean testBean = new DefaultBeanCollectionUtilsTest.TestBean();
        added = instance.putAllMap(testBean::getMap, testBean::setMap, null);
        assertNull(added);
        Map<Integer, Integer> map = new HashMap<>();
        added = instance.putAllMap(testBean::getMap, testBean::setMap, map);
        assertNull(added);
    }

    protected BeanCollectionAddUtils testedInstance() {
        return new DefaultBeanCollectionUtils();
    }
}