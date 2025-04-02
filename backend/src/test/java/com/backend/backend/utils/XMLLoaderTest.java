package com.backend.backend.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

public class XMLLoaderTest {

    @Test
    public void testParseFile_ValidXML() throws Exception {
        String xmlContent = "<root>"
                          + "  <child>value</child>"
                          + "  <child1><innerchild>value1</innerchild></child1>"
                          + "</root>";
        MockMultipartFile file = new MockMultipartFile("file", "test.xml", "text/xml", xmlContent.getBytes());
        XMLLoader loader = new XMLLoader();

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
    
    @Test
    public void testParseFile_InvalidXML() {
        // Given: An invalid XML string (missing closing tag)
        String invalidXML = "<root><child>value</child>";
        MockMultipartFile file = new MockMultipartFile("file", "invalid.xml", "text/xml", invalidXML.getBytes());
        XMLLoader loader = new XMLLoader();
        
        // Then: Parsing should throw an exception
        Exception exception = assertThrows(Exception.class, () -> {
            loader.parseFile(file);
        });
        assertNotNull(exception);
    }
}
