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
    }
}
