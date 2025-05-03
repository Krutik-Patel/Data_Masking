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
import com.backend.backend.utils.writer.DataWriter;

public class JSONLoaderTest {
    // @Test
    public void testParseFile() throws Exception {
        String jsonContent = "{\"child\": \"value\", \"child1\": { \"innerchild\": \"value1\" } }";
        String jsonContent2 = "{\"child\": \"value\", \"child1\": [ \"value1\", \"value2\" ]}";

        MockMultipartFile file = new MockMultipartFile("file", "test.json", "text/json", jsonContent.getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file", "test2.json", "text/json", jsonContent2.getBytes());
        JSONLoader loader = new JSONLoader();

        UnifiedHeirarchicalObject root = loader.parseFile(file);

        assertNotNull(root);
        assertEquals("child", root.getKey());
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
        assertEquals(3, children2.size());

        // Validate first child
        UnifiedHeirarchicalObject child12 = children2.get(0);
        assertEquals("child", child12.getKey());
        assertEquals("value", child12.getValue());
        assertTrue(child12.getChildren().isEmpty());

        // Validate second child
        UnifiedHeirarchicalObject child22 = children2.get(1);
        assertEquals("child1", child22.getKey());
        assertEquals("value1", child22.getValue());
        
        UnifiedHeirarchicalObject child23 = children2.get(2);
        assertEquals("child1", child23.getKey());
        assertEquals("value2", child23.getValue());
    }

    // @Test
    public void testParseFileWithArrayOfObjects() throws Exception {
        // JSON structure to test
        String jsonContent = """
        {
            "child": "value",
            "child1": [
                { "innerchild": "value1" },
                { "innerchild": "value2" }
            ]
        }
        """;

        // Mock the file
        MockMultipartFile file = new MockMultipartFile("file", "test.json", "text/json", jsonContent.getBytes());

        // Create an instance of JSONLoader
        JSONLoader loader = new JSONLoader();

        // Parse the file
        UnifiedHeirarchicalObject root = loader.parseFile(file);
        String outTree = new DataWriter().writeToString(root, DataWriter.DataFormat.JSON);
        System.out.println("OUT TREE: " + outTree);

        // Validate root node
        assertNotNull(root);
        assertEquals("child", root.getKey());
        assertNull(root.getValue());

        // Validate children of root
        List<UnifiedHeirarchicalObject> children = root.getChildren();
        assertEquals(3, children.size());

        // Validate first child
        UnifiedHeirarchicalObject child1 = children.get(0);
        assertEquals("child", child1.getKey());
        assertEquals("value", child1.getValue());
        assertTrue(child1.getChildren().isEmpty());

        // Validate second child
        UnifiedHeirarchicalObject child2 = children.get(1);
        assertEquals("child1", child2.getKey());
        assertEquals("innerchild", child2.getNthChild(0).getKey());
        assertEquals("value1", child2.getNthChild(0).getValue());

        UnifiedHeirarchicalObject child3 = children.get(2);
        assertEquals("child1", child3.getKey());
        assertEquals("innerchild", child3.getNthChild(0).getKey());
        assertEquals("value2", child3.getNthChild(0).getValue());
        assertTrue(child2.getNthChild(0).getChildren().isEmpty());
    }

    // @Test
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