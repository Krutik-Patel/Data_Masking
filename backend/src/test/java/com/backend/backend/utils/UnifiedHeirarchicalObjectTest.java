package com.backend.backend.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class UnifiedHeirarchicalObjectTest {
    @Test
    public void testGetSetKeyValue() {
        // test the get key, get value, set value
        UnifiedHeirarchicalObject object = new UnifiedHeirarchicalObject("root", "value");
        assertEquals("root", object.getKey());
        assertEquals("value", object.getValue());
        String newValue = "valueChanged";
        object.setValue(newValue);
        assertEquals(newValue, object.getValue());

    }

    @Test
    public void testChildFunctions() throws Exception {
        UnifiedHeirarchicalObject object = new UnifiedHeirarchicalObject("root", "value");
        object.setXpath("/root");
        UnifiedHeirarchicalObject child = new UnifiedHeirarchicalObject("child1", "childvalue");
        child.setXpath("/root/child1");
        object.addChild(child);
        assertFalse(object.getChildren().isEmpty());
        assertEquals("child1", object.getChildren().get(0).getKey());
        assertEquals("childvalue", object.getChildren().get(0).getValue());
    }

    @Test
    public void testGetChildByXpath() throws Exception {
        UnifiedHeirarchicalObject root = new UnifiedHeirarchicalObject("root", "value");
        root.setXpath("/root");
        UnifiedHeirarchicalObject child = new UnifiedHeirarchicalObject("child1", "childvalue");
        child.setXpath("/root/child1");
        root.addChild(child);

        UnifiedHeirarchicalObject fetchedChild = root.getChildByXpath("/root/child1");
        assertNotNull(fetchedChild);
        assertEquals("child1", fetchedChild.getKey());
        assertEquals("childvalue", fetchedChild.getValue());
    }

    @Test
    public void testGetChildByName() throws Exception {
        UnifiedHeirarchicalObject root = new UnifiedHeirarchicalObject("root", "value", "/root");
        UnifiedHeirarchicalObject child = new UnifiedHeirarchicalObject("child1", "childvalue", "/root/child");
        root.addChild(child);

        UnifiedHeirarchicalObject fetchedChild = root.getChildByName("child1");
        assertNotNull(fetchedChild);
        assertEquals("child1", fetchedChild.getKey());
        assertEquals("childvalue", fetchedChild.getValue());
    }

    @Test
    public void testGetNthChild() throws Exception {
        UnifiedHeirarchicalObject root = new UnifiedHeirarchicalObject("root", "value", "/root");
        UnifiedHeirarchicalObject child1 = new UnifiedHeirarchicalObject("child1", "value1", "/root/value1");
        UnifiedHeirarchicalObject child2 = new UnifiedHeirarchicalObject("child2", "value2", "/root/value2");
        root.addChild(child1);
        root.addChild(child2);

        UnifiedHeirarchicalObject firstChild = root.getNthChild(0);
        UnifiedHeirarchicalObject secondChild = root.getNthChild(1);

        assertEquals("child1", firstChild.getKey());
        assertEquals("value1", firstChild.getValue());
        assertEquals("child2", secondChild.getKey());
        assertEquals("value2", secondChild.getValue());
    }

    @Test
    public void testAddChildWithoutXpath() {
        UnifiedHeirarchicalObject root = new UnifiedHeirarchicalObject("root", "value");
        UnifiedHeirarchicalObject child = new UnifiedHeirarchicalObject("child1", "childvalue");

        Exception exception = assertThrows(Exception.class, () -> root.addChild(child));
        assertEquals("XPath not initialized... Please assign xPath to this node.", exception.getMessage());
    }

    @Test
    public void testHasChildren() throws Exception {
        UnifiedHeirarchicalObject root = new UnifiedHeirarchicalObject("root", "value");
        assertFalse(root.hasChildren());

        UnifiedHeirarchicalObject child = new UnifiedHeirarchicalObject("child1", "childvalue");
        child.setXpath("/root/child1");
        root.addChild(child);

        assertTrue(root.hasChildren());
    }
}