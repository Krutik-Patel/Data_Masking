package com.backend.backend.utils.writer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import com.backend.backend.utils.UnifiedHeirarchicalObject;

public class XMLWriterTest {
    @Test
    public void testWriteToString_LeafNode() {
        // Create a leaf node with a key and value
        UnifiedHeirarchicalObject leaf = new UnifiedHeirarchicalObject("name", "value");
        XMLWriter writer = new XMLWriter();
        String xml = writer.writeToString(leaf);
        
        // For a leaf node, no indentation is applied (level==0)
        String expected = "<name>value</name>\n";
        assertEquals(expected, xml);
    }
    
    @Test
    public void testWriteToString_NodeWithChildren() throws Exception {
        // Create a root node with no value but with children
        UnifiedHeirarchicalObject root = new UnifiedHeirarchicalObject("root", null);
        UnifiedHeirarchicalObject child1 = new UnifiedHeirarchicalObject("child", "val1");
        UnifiedHeirarchicalObject child2 = new UnifiedHeirarchicalObject("child2", "val2");
        root.setXpath("/root");
        child1.setXpath("/root/child");
        child2.setXpath("/root/child2");
        
        root.addChild(child1);
        root.addChild(child2);
        
        XMLWriter writer = new XMLWriter();
        String xml = writer.writeToString(root);
        
        // Expected XML:
        // Opening tag of root with newline, then each child indented by one tab, then closing tag.
        String expected = "<root>\n\t<child>val1</child>\n\t<child2>val2</child2>\n</root>\n";
        assertEquals(expected, xml);
    }
}
