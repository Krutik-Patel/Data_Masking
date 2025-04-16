package com.backend.backend.masks;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.backend.backend.utils.UnifiedHeirarchicalObject;

public class BinningStrategyTest {

    @Test
    public void testBinningStrategyDefaultX() {
        // Test with default x=1, bin size = 10^1 = 10
        MaskingStrategy binningMask = new BinningStrategy(null);
        List<UnifiedHeirarchicalObject> o_list = new ArrayList<>();
        o_list.add(new UnifiedHeirarchicalObject("key1", "24"));
        o_list.add(new UnifiedHeirarchicalObject("key2", "243"));
        o_list.add(new UnifiedHeirarchicalObject("key3", "0"));
        o_list.add(new UnifiedHeirarchicalObject("key4", "9.9"));
        o_list.add(new UnifiedHeirarchicalObject("key5", "10"));
        o_list.add(new UnifiedHeirarchicalObject("key6", "15.5"));
        o_list.add(new UnifiedHeirarchicalObject("key7", "-24"));

        // Apply the masking strategy
        binningMask.mask(o_list);

        // Define expected values
        List<String> expectedValues = List.of(
            "20-30",    // 24 falls in 20-30
            "240-250",  // 243 falls in 240-250
            "0-10",     // 0 falls in 0-10
            "0-10",     // 9.9 falls in 0-10
            "10-20",    // 10 falls in 10-20
            "10-20",    // 15.5 falls in 10-20
            "-30--20"   // -24 falls in -30 to -20
        );

        // Assert that each object's value matches the expected bin
        for (int i = 0; i < o_list.size(); i++) {
            assertEquals(expectedValues.get(i), o_list.get(i).getValue());
        }
    }

    @Test
    public void testBinningStrategyCustomX() {
        // Test with custom x=2, bin size = 10^2 = 100
        Map<String, Object> params = new HashMap<>();
        params.put("x", "2");
        MaskingStrategy binningMask = new BinningStrategy(params);
        List<UnifiedHeirarchicalObject> o_list = new ArrayList<>();
        o_list.add(new UnifiedHeirarchicalObject("key1", "567"));
        o_list.add(new UnifiedHeirarchicalObject("key2", "1234"));
        o_list.add(new UnifiedHeirarchicalObject("key3", "99"));
        o_list.add(new UnifiedHeirarchicalObject("key4", "100"));
        o_list.add(new UnifiedHeirarchicalObject("key5", "-24"));

        // Apply the masking strategy
        binningMask.mask(o_list);

        // Define expected values
        List<String> expectedValues = List.of(
            "500-600",   // 567 falls in 500-600
            "1200-1300", // 1234 falls in 1200-1300
            "0-100",     // 99 falls in 0-100
            "100-200",   // 100 falls in 100-200
            "-100-0"     // -24 falls in -100-0
        );

        // Assert that each object's value matches the expected bin
        for (int i = 0; i < o_list.size(); i++) {
            assertEquals(expectedValues.get(i), o_list.get(i).getValue());
        }
    }
}