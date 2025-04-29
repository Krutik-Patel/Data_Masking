package com.backend.backend.morpher;

import com.backend.backend.engine.ConfigLoader;
import com.backend.backend.engine.DataFileLoader;
import com.backend.backend.masks.MaskingStrategy;
import com.backend.backend.utils.UnifiedHeirarchicalObject;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ReferentialMaskingExecutorTest {

    // Test of the applyValueTransformationLog method
    /*
     * Parameters of the method :
     * - xPath
     * - valueTransformationLog
     * - xPathValueToDataMap
     * - foreignKeyMap
     */
    @Test
    void testApplyValueTransformationLog() {
        ReferentialMaskingExecutor executor = new ReferentialMaskingExecutor();

        String primaryXPath = "/person/id";
        String foreignXPath = "/order/personId";

        // Original nodes
        UnifiedHeirarchicalObject primary1 = new UnifiedHeirarchicalObject("person", "123", "/person/id");
        UnifiedHeirarchicalObject foreign1 = new UnifiedHeirarchicalObject("order", "123", "/order/personId");

        // Transformation log
        Map<String, String> transformationLog = Map.of("123", "XYZ");

        // xPathValueToDataMap
        Map<String, Map<String, List<UnifiedHeirarchicalObject>>> xPathValueToDataMap = new HashMap<>();

        xPathValueToDataMap.put(primaryXPath, new HashMap<>(Map.of("123", new ArrayList<>(List.of(primary1)))));
        xPathValueToDataMap.put(foreignXPath, new HashMap<>(Map.of("123", new ArrayList<>(List.of(foreign1)))));

        // Foreign key relationship: primary -> foreign
        Map<String, List<String>> foreignKeyMap = new HashMap<>();
        foreignKeyMap.put(primaryXPath, List.of(foreignXPath));

        // Run the method
        executor.applyValueTransformationLog(primaryXPath, transformationLog, xPathValueToDataMap, foreignKeyMap);

        // Assert the new maps have updated keys and node values
        Map<String, List<UnifiedHeirarchicalObject>> updatedPrimary = xPathValueToDataMap.get(primaryXPath);
        Map<String, List<UnifiedHeirarchicalObject>> updatedForeign = xPathValueToDataMap.get(foreignXPath);

        // Check that old keys are no longer present
        assertEquals(Set.of("XYZ"), updatedPrimary.keySet());
        assertEquals(Set.of("XYZ"), updatedForeign.keySet());

        // Check that the data lists are still the same size and contain the same
        // objects
        assertEquals(1, updatedPrimary.get("XYZ").size());
        assertEquals(1, updatedForeign.get("XYZ").size());

        // Optionally: confirm the data object is still the same one we put in
        assertEquals(primary1, updatedPrimary.get("XYZ").get(0));
        assertEquals(foreign1, updatedForeign.get("XYZ").get(0));
    }

    // To test whehter the foreign keys are getting recursively masked or not.
    // SUCCESS if : on running the mask on a paritcular value on primary key, all
    // those values in foreign keys are masked. The chagnes should be reflected in
    // xPathValueToData Map
    // @Test
    // void testSimpleReferentialMaskingForOneValue()
    // {

    // }

}
