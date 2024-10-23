package cz.ad.java.tools.collection.bean;

import cz.ad.java.tools.collection.core.CollectionAddUtils;
import cz.ad.java.tools.collection.core.CollectionUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class BeanCollectionUtilsTest {

    @Test
    void defaultInstance() {
        BeanCollectionUtils defaultInstance = BeanCollectionUtils.DEFAULT_INSTANCE;
        assertNotNull(defaultInstance);

        CollectionAddUtils utils = CollectionUtils.DEFAULT_INSTANCE;
        BeanCollectionUtils instance = BeanCollectionUtils.instance(utils);
        assertNotNull(instance);

        instance = BeanCollectionUtils.instance();
        assertNotNull(instance);
    }

}