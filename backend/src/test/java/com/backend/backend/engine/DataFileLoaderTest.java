package com.backend.backend.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.reflect.Field;

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

        // For the key "/root/child" we expect one node with key "child" and value
        // "value"
        List<UnifiedHeirarchicalObject> expectedChildList = new ArrayList<>();
        expectedChildList.add(new UnifiedHeirarchicalObject("child", "value"));
        expectedMap.put("/root/child", expectedChildList);

        // For the key "/root/child1/innerchild" we expect one node with key
        // "innerchild" and value "value1"
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

        String actualString = dataFileLoader.stringifyData("xml");
        String expectedString = "<root>\n\t<child>value</child>\n\t<child1>value1</child1>\n</root>\n";

        assertEquals(actualString, expectedString);
    }

    @Test
    public void testGetNodesByXPath() throws Exception {
        String xmlContent = "<root><child>value</child><child>value1</child></root>";
        MockMultipartFile file = new MockMultipartFile("test", "test.xml", "text/xml", xmlContent.getBytes());

        DataFileLoader dataFileLoader = new DataFileLoader();
        dataFileLoader.parse(file);

        List<UnifiedHeirarchicalObject> nodes = dataFileLoader.getNodesByXPath("/root/child");
        assertEquals(nodes.get(0).getValue(), "value");
        assertEquals(nodes.get(1).getValue(), "value1");
    }

    @Test
    public void testGetFullPackagedData() throws Exception {
        DataFileLoader loader = new DataFileLoader();

        // Prepare a fake xPathToData map with two xpaths, each having two nodes.
        Map<String, List<UnifiedHeirarchicalObject>> fakeMap = new HashMap<>();

        // Column 1: /root/child
        List<UnifiedHeirarchicalObject> col1 = new ArrayList<>();
        col1.add(new UnifiedHeirarchicalObject("child", "value1", "/root/child"));
        col1.add(new UnifiedHeirarchicalObject("child", "value2", "/root/child"));

        // Column 2: /root/child1
        List<UnifiedHeirarchicalObject> col2 = new ArrayList<>();
        col2.add(new UnifiedHeirarchicalObject("child1", "value3", "/root/child1"));
        col2.add(new UnifiedHeirarchicalObject("child1", "value4", "/root/child1"));

        fakeMap.put("/root/child", col1);
        fakeMap.put("/root/child1", col2);

        // Using reflection to inject the fakeMap into loader's private xPathToData
        // field
        Field field = DataFileLoader.class.getDeclaredField("xPathToData");
        field.setAccessible(true);
        field.set(loader, fakeMap);

        // Now test the getFullPackagedData method
        List<UnifiedHeirarchicalObject> packagedRows = loader.getFullPackagedData();
        assertNotNull(packagedRows);
        // There should be 2 rows (based on the size of a column)
        assertEquals(2, packagedRows.size(), "There should be 2 packaged rows.");

        // For first packaged row, expect the first element from each column
        UnifiedHeirarchicalObject row1 = packagedRows.get(0);
        List<UnifiedHeirarchicalObject> childrenRow1 = row1.getChildren();
        // Assuming each row package gets cells from each column (2 cells total)
        assertEquals(2, childrenRow1.size(), "Row 1 should have 2 children.");
        assertEquals("value3", childrenRow1.get(0).getValue(), "First cell in row 1 should be 'value1'.");
        assertEquals("value1", childrenRow1.get(1).getValue(), "Second cell in row 1 should be 'value3'.");

        // For second packaged row, expect the second element from each column
        UnifiedHeirarchicalObject row2 = packagedRows.get(1);
        List<UnifiedHeirarchicalObject> childrenRow2 = row2.getChildren();
        assertEquals(2, childrenRow2.size(), "Row 2 should have 2 children.");
        assertEquals("value4", childrenRow2.get(0).getValue(), "First cell in row 2 should be 'value2'.");
        assertEquals("value2", childrenRow2.get(1).getValue(), "Second cell in row 2 should be 'value4'.");
    }
}
