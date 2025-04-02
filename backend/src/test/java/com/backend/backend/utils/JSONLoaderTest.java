package com.backend.backend.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;

public class JSONLoaderTest {
    @Test
    public void testParseFile() throws Exception {
        String jsonContent = "{\"child\": \"value\", \"child1\": { \"innerchild\": \"value1\" } }";

        MockMultipartFile file = new MockMultipartFile("file", "test.json", "text/json", jsonContent.getBytes());
        JSONLoader loader = new JSONLoader();

        UnifiedHeirarchicalObject root = loader.parseFile(file);

        assertNotNull(root);
        assertEquals("root", root.getKey());
        assertNull(root.getValue());
        List<UnifiedHeirarchicalObject> children = root.getChildren();
        assertEquals(2, children.size());

        UnifiedHeirarchicalObject child1 = children.get(0);
        assertEquals("child", child1.getKey());
        assertEquals("value", child1.getValue());
        assertTrue(child1.getChildren().isEmpty());

        UnifiedHeirarchicalObject child2 = children.get(1);
        assertEquals("child1", child2.getKey());
        assertNull(child2.getValue());
        List<UnifiedHeirarchicalObject> child2Children = child2.getChildren();
        assertEquals(1, child2Children.size());

        UnifiedHeirarchicalObject innerChild = child2Children.get(0);
        assertEquals("innerchild", innerChild.getKey());
        assertEquals("value1", innerChild.getValue());
    }

    @Test void testParseFileInvalid() {
        String invalidJson = "{\"root\": ";
        MockMultipartFile file = new MockMultipartFile("file", "invalid.json", "text/json", invalidJson.getBytes());

        JSONLoader loader = new JSONLoader();

        Exception exception = assertThrows(Exception.class, () -> {
            loader.parseFile(file);
        });
        assertNotNull(exception);
    }
}
