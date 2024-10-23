package cz.ad.java.tools.collection;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CollectionUtilsTest {

    @Test
    void instance() {
        CollectionAddUtils instance = CollectionUtils.instance();
        assertNotNull(instance);
        assertNotNull(CollectionUtils.DEFAULT_INSTANCE);
    }
}