package com.backend.backend.masks;

import java.util.*;
import org.junit.jupiter.api.Test;

import com.backend.backend.utils.UnifiedHeirarchicalObject;
import com.backend.backend.utils.writer.DataWriter;
import com.backend.backend.utils.writer.DataWriter.DataFormat;

public class LDiversityMaskingStrategyTest {

    @Test
    public void testLDiversityMaskStrategy() throws Exception {
        List<UnifiedHeirarchicalObject> dataSlices = new ArrayList<>();

        UnifiedHeirarchicalObject r1 = new UnifiedHeirarchicalObject("package", null);
        r1.addChild(new UnifiedHeirarchicalObject("age", "25", "/data/age"));
        r1.addChild(new UnifiedHeirarchicalObject("zip", "12345", "/data/zip"));
        r1.addChild(new UnifiedHeirarchicalObject("disease", "Cancer", "/data/disease"));
        dataSlices.add(r1);

        UnifiedHeirarchicalObject r2 = new UnifiedHeirarchicalObject("package1", null);
        r2.addChild(new UnifiedHeirarchicalObject("age", "25", "/data/age"));
        r2.addChild(new UnifiedHeirarchicalObject("zip", "12345", "/data/zip"));
        r2.addChild(new UnifiedHeirarchicalObject("disease", "Flu", "/data/disease"));
        dataSlices.add(r2);

        UnifiedHeirarchicalObject r3 = new UnifiedHeirarchicalObject("package2", null);
        r3.addChild(new UnifiedHeirarchicalObject("age", "25", "/data/age"));
        r3.addChild(new UnifiedHeirarchicalObject("zip", "12345", "/data/zip"));
        r3.addChild(new UnifiedHeirarchicalObject("disease", "Cold", "/data/disease"));
        dataSlices.add(r3);

        UnifiedHeirarchicalObject r4 = new UnifiedHeirarchicalObject("package3", null);
        r4.addChild(new UnifiedHeirarchicalObject("age", "30", "/data/age"));
        r4.addChild(new UnifiedHeirarchicalObject("zip", "54321", "/data/zip"));
        r4.addChild(new UnifiedHeirarchicalObject("disease", "Cancer", "/data/disease"));
        dataSlices.add(r4);

        // Configuration
        Map<String, Object> params = new HashMap<>();
        params.put("l", "4");
        params.put("quasi_identifiers", Arrays.asList("/data/age", "/data/zip")); // Required
        params.put("sensitive_attribute", "/data/disease"); // Required

        // Apply l-diversity
        LDiversityMaskingStrategy strategy = new LDiversityMaskingStrategy(params);
        strategy.mask(dataSlices);

        // Output the masked results
        DataWriter dataWriter = new DataWriter();
        for (UnifiedHeirarchicalObject slice : dataSlices) {
            try {
                String output = dataWriter.writeToString(slice, DataFormat.XML);
                System.out.println(output);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
