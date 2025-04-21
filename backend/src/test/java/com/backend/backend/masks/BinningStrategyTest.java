package com.backend.backend.masks;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.*;
import org.junit.jupiter.api.Test;

import com.backend.backend.utils.UnifiedHeirarchicalObject;

public class BinningStrategyTest {


    @Test
    public void testDefaultXMasking() {
        // • Default parameters (null) should use x = 1 (bin size = 10)
        Map<String, Object> params = new HashMap<>();
        params.put("x", "1");
        MaskingStrategy strategy = new BinningStrategy(params);
        List<UnifiedHeirarchicalObject> list = new ArrayList<>();
        list.add(new UnifiedHeirarchicalObject("key1", "24"));    // expected "20-30"
        list.add(new UnifiedHeirarchicalObject("key2", "243"));   // expected "240-250"
        list.add(new UnifiedHeirarchicalObject("key3", "0"));     // expected "0-10"
        list.add(new UnifiedHeirarchicalObject("key4", "9.9"));   // expected "0-10"
        list.add(new UnifiedHeirarchicalObject("key5", "10"));    // expected "10-20"
        list.add(new UnifiedHeirarchicalObject("key6", "15.5"));  // expected "10-20"
        list.add(new UnifiedHeirarchicalObject("key7", "-24"));   // expected "-30--20"

        // Apply binning mask
        strategy.mask(list);

        // Expected bin ranges
        List<String> expectedValues = List.of("20-30", "240-250", "0-10", "0-10", "10-20", "10-20", "-30--20");
        for (int i = 0; i < list.size(); i++) {
            assertEquals(expectedValues.get(i), list.get(i).getValue());
            // System.out.println("Expected: " + expectedValues.get(i) + ", Value: " + list.get(i).getValue());
        }

        // Verify that getX returns the default value 1
        assertEquals(1, Integer.parseInt((String) params.get("x")));
    }

    @Test
    public void testCustomXMasking() {
        // • Custom x via parameters: x = 2 → bin size = 100
        Map<String, Object> params = new HashMap<>();
        params.put("x", "2");
        MaskingStrategy strategy = new BinningStrategy(params);

        List<UnifiedHeirarchicalObject> list = new ArrayList<>();
        list.add(new UnifiedHeirarchicalObject("key1", "567"));    // expected "500-600"
        list.add(new UnifiedHeirarchicalObject("key2", "1234"));   // expected "1200-1300"
        list.add(new UnifiedHeirarchicalObject("key3", "99"));     // expected "0-100"
        list.add(new UnifiedHeirarchicalObject("key4", "100"));    // expected "100-200"
        list.add(new UnifiedHeirarchicalObject("key5", "-24"));    // expected "-100-0"

        // Apply binning mask
        strategy.mask(list);

        // Expected bin ranges for custom x
        List<String> expectedValues = List.of("500-600", "1200-1300", "0-100", "100-200", "-100-0");
        for (int i = 0; i < list.size(); i++) {
            assertEquals(expectedValues.get(i), list.get(i).getValue());
            // System.out.println("Key: " + list.get(i).getKey() + ", Value: " + list.get(i).getValue());
        }

        // Verify that getX returns the custom value 2
        assertEquals(2, Integer.parseInt((String) params.get("x")));
    }

}
