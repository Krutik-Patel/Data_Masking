package com.backend.backend.masks;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.backend.backend.utils.UnifiedHeirarchicalObject;

public class RangeShiftStrategyTest {
    @Test
    public void testRangeShiftStrategy() {
        UnifiedHeirarchicalObject o1 = new UnifiedHeirarchicalObject("key1", "230");
        UnifiedHeirarchicalObject o2 = new UnifiedHeirarchicalObject("key2", "140.99");
        List<UnifiedHeirarchicalObject> o_list = new ArrayList<>();
        o_list.add(o1);
        o_list.add(o2);

        MaskingStrategy rangeshiftmask = new RangeShiftStrategy(null);
        rangeshiftmask.mask(o_list);

        assertNotEquals(o1.getValue(), "230");
        assertNotEquals(o2.getValue(), "140.99");

        double st1 = 230, st2 = (double) 140.99;

        assertTrue(Math.abs(Double.parseDouble(o1.getValue()) - st1) / st1 <= 0.1);
        assertTrue(Math.abs(Double.parseDouble(o2.getValue()) - st2) / st2 <= 0.1);

    }
}
