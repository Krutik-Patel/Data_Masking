package com.backend.backend.masks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;

import org.junit.jupiter.api.Test;

import com.backend.backend.utils.UnifiedHeirarchicalObject;
import com.backend.backend.utils.writer.DataWriter;
import com.backend.backend.utils.writer.DataWriter.DataFormat;

public class KAnonymizationMaskingStrategyTest {
    // @Test
    public void testkanonymizationmaskstartegy() throws Exception {
        List<UnifiedHeirarchicalObject> dataSlices = new ArrayList<>();
        UnifiedHeirarchicalObject r1 = new UnifiedHeirarchicalObject("package", null);
        r1.addChild(new UnifiedHeirarchicalObject("age", "25"));
        r1.addChild(new UnifiedHeirarchicalObject("zip", "12345"));
        r1.addChild(new UnifiedHeirarchicalObject("gender", "M"));
        dataSlices.add(r1);

        UnifiedHeirarchicalObject r2 = new UnifiedHeirarchicalObject("package1", null);
        r2.addChild(new UnifiedHeirarchicalObject("age", "26"));
        r2.addChild(new UnifiedHeirarchicalObject("zip", "12346"));
        r2.addChild(new UnifiedHeirarchicalObject("gender", "M"));
        dataSlices.add(r2);

        UnifiedHeirarchicalObject r3 = new UnifiedHeirarchicalObject("package2", null);
        r3.addChild(new UnifiedHeirarchicalObject("age", "30"));
        r3.addChild(new UnifiedHeirarchicalObject("zip", "54321"));
        r3.addChild(new UnifiedHeirarchicalObject("gender", "F"));
        dataSlices.add(r3);

        UnifiedHeirarchicalObject r4 = new UnifiedHeirarchicalObject("package3", null);
        r4.addChild(new UnifiedHeirarchicalObject("age", "40"));
        r4.addChild(new UnifiedHeirarchicalObject("zip", "12321"));
        r4.addChild(new UnifiedHeirarchicalObject("gender", "F"));
        dataSlices.add(r4);

        // Configuration
        Map<String, Object> params = new HashMap<>();
        params.put("k", "4");

        // Apply k-anonymity
        KAnonymizationMaskingStrategy strategy = new KAnonymizationMaskingStrategy(params);
        strategy.mask(dataSlices);

        DataWriter datawriter = new DataWriter();
        try {
            dataSlices.forEach((slice) -> {
                String output;
                try {
                    output = datawriter.writeToString(slice, DataFormat.XML);
                    System.err.println(output);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.out.println(e.toString());
                }
            });
        } catch(Exception e) {
            System.out.println("idlk");
        }
    }
}
