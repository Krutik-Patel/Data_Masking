package com.backend.backend.masks;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.backend.backend.utils.UnifiedHeirarchicalObject;

public class NoiseInjectionStrategyTest {

    @Test
    public void testNoiseInjectionStrategy() {
        MaskingStrategy noiseMask = new NoiseInjectionStrategy(null);

        List<UnifiedHeirarchicalObject> dataSlices = new ArrayList<>();
        dataSlices.add(new UnifiedHeirarchicalObject("key1", "john doe"));
        dataSlices.add(new UnifiedHeirarchicalObject("key3", "Noise Injection Test"));

        noiseMask.mask(dataSlices);

        List<String> expectedValues = List.of(
            "j0hn d03",              // o->0, e->3
            "N0453 4nj3ct40n T35t"   // o->0, e->3, i->1, s->5, I->1, S->5, E->3
        );

        for (int i = 0; i < dataSlices.size(); i++) {
            assertEquals(expectedValues.get(i), dataSlices.get(i).getValue(),
                "Mismatch at index " + i + ": expected " + expectedValues.get(i) +
                ", got " + dataSlices.get(i).getValue());
        }
    }
}
