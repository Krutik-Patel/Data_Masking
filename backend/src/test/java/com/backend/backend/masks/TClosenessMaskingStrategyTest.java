package com.backend.backend.masks;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.*;
import org.junit.jupiter.api.Test;

import com.backend.backend.utils.UnifiedHeirarchicalObject;

public class TClosenessMaskingStrategyTest {

    @Test
    public void testTClosenessMaskStrategy() throws Exception {
        // Build 6 records
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
        r3.addChild(new UnifiedHeirarchicalObject("age", "25", "/data/age"));
        r3.addChild(new UnifiedHeirarchicalObject("zip", "12345", "/data/zip"));
        r3.addChild(new UnifiedHeirarchicalObject("gender", "F", "/data/gender"));
        dataSlices.add(r3);

        UnifiedHeirarchicalObject r4 = new UnifiedHeirarchicalObject("package3", null);
        r4.addChild(new UnifiedHeirarchicalObject("age", "30", "/data/age"));
        r4.addChild(new UnifiedHeirarchicalObject("zip", "54321", "/data/zip"));
        r4.addChild(new UnifiedHeirarchicalObject("gender", "F", "/data/gender"));
        dataSlices.add(r4);

        UnifiedHeirarchicalObject r5 = new UnifiedHeirarchicalObject("package4", null);
        r5.addChild(new UnifiedHeirarchicalObject("age", "30", "/data/age"));
        r5.addChild(new UnifiedHeirarchicalObject("zip", "54321", "/data/zip"));
        r5.addChild(new UnifiedHeirarchicalObject("gender", "F", "/data/gender"));
        dataSlices.add(r5);

        UnifiedHeirarchicalObject r6 = new UnifiedHeirarchicalObject("package5", null);
        r6.addChild(new UnifiedHeirarchicalObject("age", "35", "/data/age"));
        r6.addChild(new UnifiedHeirarchicalObject("zip", "67890", "/data/zip"));
        r6.addChild(new UnifiedHeirarchicalObject("gender", "M", "/data/gender"));
        dataSlices.add(r6);

        // Configuration: t=0.2, quasi-identifiers=[age, zip], sensitive_identifiers=[gender]
        Map<String, Object> params = new HashMap<>();
        params.put("t", "0.2");
        params.put("quasi_identifiers", Arrays.asList("/data/age", "/data/zip"));
        params.put("sensitive_identifiers", Arrays.asList("/data/gender"));
        TClosenessMaskingStrategy strategy = new TClosenessMaskingStrategy(params);

        // Apply t-closeness
        strategy.mask(dataSlices);

        // Expected [age, zip, gender] for each record after masking:
        // Overall distribution: 3M, 3F (50% each)
        // Group1 [25, 12345]: 2M, 1F (distance=0.1667 < 0.2) → gender unchanged
        // Group2 [30, 54321]: 2F (distance=0.5 > 0.2) → gender masked
        // Group3 [35, 67890]: 1M (distance=0.5 > 0.2) → gender masked
        List<String[]> expected = List.of(
            new String[]{"25", "12345", "M"},
            new String[]{"25", "12345", "M"},
            new String[]{"25", "12345", "F"},
            new String[]{"30", "54321", "*"},
            new String[]{"30", "54321", "*"},
            new String[]{"35", "67890", "*"}
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
                         "gender mismatch at record " + (i+1));
        }

        // Verify that the parameter 't' remains set to "0.2"
        assertEquals("0.2", params.get("t"));
    }
}