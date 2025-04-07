package com.backend.backend.masks;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.backend.backend.utils.UnifiedHeirarchicalObject;

public class RedactionMaskingStrategyTest {
    @Test
    public void testRedactionMask() {
        UnifiedHeirarchicalObject o1 = new UnifiedHeirarchicalObject("key1", "3212-4421-2212-2222");
        UnifiedHeirarchicalObject o2 = new UnifiedHeirarchicalObject("key2", "3445233242");
        List<UnifiedHeirarchicalObject> o_list = new ArrayList<>();
        o_list.add(o1);
        o_list.add(o2);

        MaskingStrategy redactionMaskingStrategy = new RedactionMaskingStrategy(null);
        redactionMaskingStrategy.mask(o_list);

        assertEquals(o1.getValue(), "REDACTED");
        assertEquals(o2.getValue(), "REDACTED");
    }
}
