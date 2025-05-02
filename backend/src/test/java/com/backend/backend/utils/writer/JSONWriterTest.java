package com.backend.backend.utils.writer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import com.backend.backend.utils.UnifiedHeirarchicalObject;

public class JSONWriterTest {

    @Test
    public void testWriteToString_LeafNode() {
        UnifiedHeirarchicalObject leaf = new UnifiedHeirarchicalObject("name", "value");
        JSONWriter writer = new JSONWriter();
        String json = writer.writeToString(leaf);

        // String expected = "\"name\": \"value\"";
        String expected = "{\n" +
                          "\t\"name\": \"value\"\n" +
                          "}";
        assertEquals(expected, json);
    }

    @Test
    public void testWriteToString_NodeWithChildren() throws Exception {
        UnifiedHeirarchicalObject root = new UnifiedHeirarchicalObject("root", null);
        UnifiedHeirarchicalObject child1 = new UnifiedHeirarchicalObject("child", "val1");
        UnifiedHeirarchicalObject child2 = new UnifiedHeirarchicalObject("child2", "val2");

        root.setXpath("/root");
        child1.setXpath("/root/child");
        child2.setXpath("/root/child2");

        root.addChild(child1);
        root.addChild(child2);

        JSONWriter writer = new JSONWriter();
        String json = writer.writeToString(root);

        // String expected = "\"root\": {\n" +
        //         "\t\"child\": \"val1\",\n" +
        //         "\t\"child2\": \"val2\"\n" +
        //         "}";
        String expected = "{\n" +
                    "\t\"root\": {\n" +
                    "\t\t\"child\": \"val1\",\n" +
                    "\t\t\"child2\": \"val2\"\n" +
                    "\t}\n" +
                    "}";

        assertEquals(expected, json);
    }
}
