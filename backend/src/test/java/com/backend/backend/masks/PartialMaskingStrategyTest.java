package com.backend.backend.masks;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.backend.backend.utils.UnifiedHeirarchicalObject;

public class PartialMaskingStrategyTest {
    @Test
    public void testPartialMaskingStrategy() {
        // Test 1: x = 4
        Map<String, Object> params = new HashMap<>();
        params.put("x", 4);
        MaskingStrategy partialMask = new PartialMaskingStrategy(params);
        List<UnifiedHeirarchicalObject> oList = new ArrayList<>();
        oList.add(new UnifiedHeirarchicalObject("key1", "123456789"));
        oList.add(new UnifiedHeirarchicalObject("key2", "abcde"));
        oList.add(new UnifiedHeirarchicalObject("key3", "hello world"));

        partialMask.mask(oList);

        assertEquals("XXXX56789", oList.get(0).getValue());
        assertEquals("XXXXe", oList.get(1).getValue());
        assertEquals("XXXXo world", oList.get(2).getValue());

        // Test 2: x = 9 (equal to string length)
        params.put("x", 9);
        partialMask = new PartialMaskingStrategy(params);
        oList.clear();
        oList.add(new UnifiedHeirarchicalObject("key1", "123456789"));
        oList.add(new UnifiedHeirarchicalObject("key2", "abcde"));

        partialMask.mask(oList);

        assertEquals("XXXXXXXXX", oList.get(0).getValue());
        assertEquals("XXXXX", oList.get(1).getValue());

        // Test 3: x = 0 (no masking)
        params.put("x", 0);
        partialMask = new PartialMaskingStrategy(params);
        oList.clear();
        oList.add(new UnifiedHeirarchicalObject("key1", "123456789"));

        partialMask.mask(oList);

        assertEquals("123456789", oList.get(0).getValue());

        // Test 4: Default x = 4 when params is null
        partialMask = new PartialMaskingStrategy(null);
        oList.clear();
        oList.add(new UnifiedHeirarchicalObject("key1", "123456789"));

        partialMask.mask(oList);

        assertEquals("XXXX56789", oList.get(0).getValue());
    }
}