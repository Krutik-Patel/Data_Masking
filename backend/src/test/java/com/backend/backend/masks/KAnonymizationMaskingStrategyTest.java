package com.backend.backend.masks;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.*;
import org.junit.jupiter.api.Test;

import com.backend.backend.utils.UnifiedHeirarchicalObject;

public class KAnonymizationMaskingStrategyTest {

    @Test
    public void testKAnonymizationMaskStrategy() throws Exception {
        // Build 4 records
        List<UnifiedHeirarchicalObject> dataSlices = new ArrayList<>();

        UnifiedHeirarchicalObject r1 = new UnifiedHeirarchicalObject("package", null);
        r1.addChild(new UnifiedHeirarchicalObject("age", "25", "/data/age"));
        r1.addChild(new UnifiedHeirarchicalObject("zip", "12345", "/data/zip"));
        r1.addChild(new UnifiedHeirarchicalObject("gender", "M", "/data/gender"));
        dataSlices.add(r1);

        UnifiedHeirarchicalObject r2 = new UnifiedHeirarchicalObject("package1", null);
        r2.addChild(new UnifiedHeirarchicalObject("age", "25", "/data/age"));
        r2.addChild(new UnifiedHeirarchicalObject("zip", "12345", "/data/zip"));
        r2.addChild(new UnifiedHeirarchicalObject("gender", "M", "/data/gender"));
        dataSlices.add(r2);

        UnifiedHeirarchicalObject r3 = new UnifiedHeirarchicalObject("package2", null);
        r3.addChild(new UnifiedHeirarchicalObject("age", "30", "/data/age"));
        r3.addChild(new UnifiedHeirarchicalObject("zip", "54321", "/data/zip"));
        r3.addChild(new UnifiedHeirarchicalObject("gender", "F", "/data/gender"));
        dataSlices.add(r3);

        UnifiedHeirarchicalObject r4 = new UnifiedHeirarchicalObject("package3", null);
        r4.addChild(new UnifiedHeirarchicalObject("age", "25", "/data/age"));
        r4.addChild(new UnifiedHeirarchicalObject("zip", "12321", "/data/zip"));
        r4.addChild(new UnifiedHeirarchicalObject("gender", "F", "/data/gender"));
        dataSlices.add(r4);

        // Configuration: k = 2, quasi-identifiers = [age, zip]
        Map<String, Object> params = new HashMap<>();
        params.put("k", "2");
        params.put("quasi_identifiers", Arrays.asList("/data/age", "/data/zip"));
        KAnonymizationMaskingStrategy strategy = new KAnonymizationMaskingStrategy(params);

        // Apply k-anonymity
        strategy.mask(dataSlices);

        // Expected [age, zip, gender] for each record after masking:
        //  r1, r2 are in a group of size=2 → kept
        //  r3, r4 are in groups of size=1 → age & zip masked, gender untouched
        List<String[]> expected = List.of(
            new String[]{"25",   "12345", "M"},
            new String[]{"25",   "12345", "M"},
            new String[]{"*",    "*",     "F"},
            new String[]{"*",    "*",     "F"}
        );

        for (int i = 0; i < dataSlices.size(); i++) {
            UnifiedHeirarchicalObject rec = dataSlices.get(i);
            assertEquals(expected.get(i)[0],
                         rec.getChildByXpath("/data/age").getValue(),
                         "age mismatch at record " + (i+1));
            assertEquals(expected.get(i)[1],
                         rec.getChildByXpath("/data/zip").getValue(),
                         "zip mismatch at record " + (i+1));
            assertEquals(expected.get(i)[2],
                         rec.getChildByXpath("/data/gender").getValue(),
                         "gender should remain unchanged at record " + (i+1));
        }

        // Verify that the parameter 'k' remains set to "2"
        assertEquals("2", params.get("k"));
    }
}
