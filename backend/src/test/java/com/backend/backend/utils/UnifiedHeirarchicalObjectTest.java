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
    public void testChildFunctions() {
        UnifiedHeirarchicalObject object = new UnifiedHeirarchicalObject("root", "value");
        UnifiedHeirarchicalObject child = new UnifiedHeirarchicalObject("child1", "childvalue");
        object.addChild(child);
        assertFalse(object.getChildren().isEmpty());
        assertEquals("child1", object.getChildren().get(0).getKey());
    }
}
