package com.backend.backend.utils.writer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.backend.backend.utils.UnifiedHeirarchicalObject;
import com.backend.backend.utils.writer.DataWriter.DataFormat;

public class DataWriterTest {
    @Test
    public void testXMLDataWriter() throws Exception {
        UnifiedHeirarchicalObject root = new UnifiedHeirarchicalObject("root", null);
        UnifiedHeirarchicalObject child1 = new UnifiedHeirarchicalObject("child", "val1");
        UnifiedHeirarchicalObject child2 = new UnifiedHeirarchicalObject("child2", "val2");
        root.setXpath("/root");
        child1.setXpath("/root/child");
        child2.setXpath("/root/child2");


        root.addChild(child1);
        root.addChild(child2);

        String writtenData = new DataWriter().writeToString(root, DataFormat.XML);
        String expected = "<root>\n\t<child>val1</child>\n\t<child2>val2</child2>\n</root>\n";

        assertEquals(expected, writtenData);
    }

    // @Test
    // public void testJSONDataWriter() throws Exception {
        
    // }

    // @Test
    // public void testDumpFunction() {

    // }
}
