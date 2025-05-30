package com.backend.backend.utils.loader;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import com.backend.backend.utils.UnifiedHeirarchicalObject;
import com.backend.backend.utils.loader.DataLoader;

public class DataLoaderTest {
    @Test
    public void testloadXML() throws Exception {
        String xmlContent = "<root><child>value</child><child1><innerchild>value1</innerchild></child1></root>";

        MockMultipartFile file = new MockMultipartFile("file", "test.xml", "text/xml", xmlContent.getBytes());

        DataLoader loader = new DataLoader();
        UnifiedHeirarchicalObject result = loader.loadContent(file);
        assertNotNull(result);
        assertEquals("root", result.getKey());
        assertNull(result.getValue());
        assertTrue(result.hasChildren());

        List<UnifiedHeirarchicalObject> children = result.getChildren();
        assertEquals("child", children.get(0).getKey());
        assertEquals("value1", children.get(1).getChildren().get(0).getValue());
    }

    public void testloadJSON() throws Exception {
        String jsonContent = "{\"child\": \"value\", \"child1\": { \"innerchild\": \"value1\" } }";
        
        MockMultipartFile file = new MockMultipartFile("file", "test.json", "text/json", jsonContent.getBytes());

        DataLoader loader = new DataLoader();
        UnifiedHeirarchicalObject result = loader.loadContent(file);

        assertNotNull(result);
        assertEquals("root", result.getKey());
        assertNull(result.getValue());
        assertTrue(result.hasChildren());

        List<UnifiedHeirarchicalObject> children = result.getChildren();
        assertEquals("child", children.get(0).getKey());
        assertEquals("value1", children.get(1).getChildren().get(0).getValue());
    }
}
