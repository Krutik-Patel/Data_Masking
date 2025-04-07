package com.backend.backend.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import com.backend.backend.utils.UnifiedHeirarchicalObject;

public class DataFileLoaderTest {
    @Test
    public void testParseFunction() throws Exception {
        String xmlContent = "<root><child>value</child><child1><innerchild>value1</innerchild></child1></root>";
        MockMultipartFile file = new MockMultipartFile("test", "test.xml", "text/xml", xmlContent.getBytes());
        
        DataFileLoader dataFileLoader = new DataFileLoader();
        dataFileLoader.parse(file);

        // Create an expected map.
        Map<String, List<UnifiedHeirarchicalObject>> expectedMap = new HashMap<>();

        // For the key "/root/child" we expect one node with key "child" and value "value"
        List<UnifiedHeirarchicalObject> expectedChildList = new ArrayList<>();
        expectedChildList.add(new UnifiedHeirarchicalObject("child", "value"));
        expectedMap.put("/root/child", expectedChildList);

        // For the key "/root/child1/innerchild" we expect one node with key "innerchild" and value "value1"
        List<UnifiedHeirarchicalObject> expectedInnerChildList = new ArrayList<>();
        expectedInnerChildList.add(new UnifiedHeirarchicalObject("innerchild", "value1"));
        expectedMap.put("/root/child1/innerchild", expectedInnerChildList);

        // Retrieve the actual map
        Map<String, List<UnifiedHeirarchicalObject>> actualMap = dataFileLoader.getXPathToDataMap();

        // Assert that both maps have the same keys
        for (String key : expectedMap.keySet()) {
            assertTrue(actualMap.containsKey(key), "Expected key not found: " + key);

            List<UnifiedHeirarchicalObject> expectedNodes = expectedMap.get(key);
            List<UnifiedHeirarchicalObject> actualNodes = actualMap.get(key);

            assertEquals(expectedNodes.size(), actualNodes.size(), "Mismatch in number of nodes for key: " + key);

            // Compare each node's key and value
            for (int i = 0; i < expectedNodes.size(); i++) {
                UnifiedHeirarchicalObject expectedNode = expectedNodes.get(i);
                UnifiedHeirarchicalObject actualNode = actualNodes.get(i);
                assertEquals(expectedNode.getKey(), actualNode.getKey(), "Key mismatch for key: " + key);
                assertEquals(expectedNode.getValue(), actualNode.getValue(), "Value mismatch for key: " + key);
            }
        }
    }

    @Test
    public void testStringify() throws Exception {
        String xmlContent = "<root><child>value</child><child1>value1</child1></root>";
        MockMultipartFile file = new MockMultipartFile("test", "test.xml", "text/xml", xmlContent.getBytes());
        
        DataFileLoader dataFileLoader = new DataFileLoader();
        dataFileLoader.parse(file);

        String actualString = dataFileLoader.stringifyData();
        String expectedString = "<root>\n\t<child>value</child>\n\t<child1>value1</child1>\n</root>\n";

        assertEquals(actualString, expectedString);
    }

    public void testGetNodesByXPath() throws Exception {
        String xmlContent = "<root><child>value</child><child>value1<child></root>";
        MockMultipartFile file = new MockMultipartFile("test", "test.xml", "text/xml", xmlContent.getBytes());

        DataFileLoader dataFileLoader = new DataFileLoader();
        dataFileLoader.parse(file);

        List<UnifiedHeirarchicalObject> nodes = dataFileLoader.getNodesByXPath("/root/child");
        assertEquals(nodes.get(0).getValue(), "value");
        assertEquals(nodes.get(1).getValue(), "value1");
    }
}
