package com.backend.backend.masks;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.backend.backend.utils.UnifiedHeirarchicalObject;

public class HashMaskingStrategyTest {
    @Test
    public void testHashMaskingStrategy() {
        MaskingStrategy hashMask = new HashMaskingStrategy(null);
        UnifiedHeirarchicalObject o1 = new UnifiedHeirarchicalObject("key1", "value1");
        UnifiedHeirarchicalObject o2 = new UnifiedHeirarchicalObject("key2", "Employee_duty_43");
        List<UnifiedHeirarchicalObject> o_list = new ArrayList<>();
        o_list.add(o1);
        o_list.add(o2);
        
        UnifiedHeirarchicalObject expected_o1 = new UnifiedHeirarchicalObject("key1", "3c9683017f9e4bf33d0fbedd26bf143fd72de9b9dd145441b75f0604047ea28e");
        UnifiedHeirarchicalObject expected_o2 = new UnifiedHeirarchicalObject("key2", "4bf2102cc38e27b4aeb054e39472a4351cd21b239122d7b60bea443dfb5c45d9");
        List<UnifiedHeirarchicalObject> expected_o_list = new ArrayList<>();
        expected_o_list.add(expected_o1);
        expected_o_list.add(expected_o2);
        
        hashMask.mask(o_list);
        
        assertEquals(expected_o1.getValue(), o1.getValue());
        assertEquals(expected_o2.getValue(), o2.getValue());
    }
}
