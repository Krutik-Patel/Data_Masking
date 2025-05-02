package com.backend.backend.utils.loader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;

import com.backend.backend.utils.UnifiedHeirarchicalObject;
import com.backend.backend.utils.loader.JSONLoader;

public class JSONLoaderTest {
    @Test
    public void testParseFile() throws Exception {
        String jsonContent = "{\"child\": \"value\", \"child1\": { \"innerchild\": \"value1\" } }";
        String jsonContent2 = "{\"child\": \"value\", \"child1\": [ \"value1\", \"value2\" ]}";

        MockMultipartFile file = new MockMultipartFile("file", "test.json", "text/json", jsonContent.getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file", "test2.json", "text/json", jsonContent2.getBytes());
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

        UnifiedHeirarchicalObject root2 = loader.parseFile(file2);

        // Validate root node
        assertNotNull(root2);
        assertEquals("root", root2.getKey());
        assertNull(root2.getValue());

        // Validate children of root
        List<UnifiedHeirarchicalObject> children2 = root2.getChildren();
        assertEquals(2, children2.size());

        // Validate first child
        UnifiedHeirarchicalObject child12 = children2.get(0);
        assertEquals("child", child12.getKey());
        assertEquals("value", child12.getValue());
        assertTrue(child12.getChildren().isEmpty());

        // Validate second child
        UnifiedHeirarchicalObject child22 = children2.get(1);
        assertEquals("child1", child22.getKey());
        assertNull(child22.getValue());

        // Validate children of the second child (array elements)
        List<UnifiedHeirarchicalObject> child2Children2 = child22.getChildren();
        assertEquals(2, child2Children2.size());

        // Validate first element in the array
        UnifiedHeirarchicalObject innerChild1 = child2Children2.get(0);
        System.out.println("ATTENTION::: " + innerChild1.getKey() + " " + innerChild1.getValue());
        assertEquals("child1", innerChild1.getKey());
        assertEquals("value1", innerChild1.getValue());

        // Validate second element in the array
        UnifiedHeirarchicalObject innerChild2 = child2Children2.get(1);
        assertEquals("child1", innerChild2.getKey());
        assertEquals("value2", innerChild2.getValue());
    }

    @Test
    void testParseFileInvalid() {
        String invalidJson = "{\"root\": ";
        MockMultipartFile file = new MockMultipartFile("file", "invalid.json", "text/json", invalidJson.getBytes());

        JSONLoader loader = new JSONLoader();

        Exception exception = assertThrows(Exception.class, () -> {
            loader.parseFile(file);
        });
        assertNotNull(exception);
    }
}

// <root>
// <child>value</child>
// <child1>
// <innerchild>value1</innerchild>
// </child1>
// </root>

// <root>
// <child>value</child>
// <child1>
// <child1>
// <innerchild>value1</innerchild>
// </child1>
// </child1>
// </root>

// {
// "child": "value",
// "child1": [
// "innerchild": "value1",
// "innerchild": "value2"
// ]
// }