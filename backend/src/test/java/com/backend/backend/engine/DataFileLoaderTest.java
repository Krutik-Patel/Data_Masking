package com.backend.backend.engine;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

public class DataFileLoaderTest {
    @Test
    public void testParseFunction() {
        String xmlContent = "<root><child>value</child><child1><innerchild>value1</innerchild></child1></root>";
        MockMultipartFile file = new MockMultipartFile("test", "test.xml", "text/xml", xmlContent.getBytes());
        
        
    }
}
