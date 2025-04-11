package com.backend.backend.masks;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.backend.backend.utils.UnifiedHeirarchicalObject;

public class NoiseInjectionStrategyTest {
    // @Test
    public void testNoiseInjectionStrategy() {
        // Initialize the NoiseInjectionStrategy with null parameters
        MaskingStrategy noiseMask = new NoiseInjectionStrategy(null);
        
        // Create a list of UnifiedHeirarchicalObject with sample data
        List<UnifiedHeirarchicalObject> dataSlices = new ArrayList<>();
        dataSlices.add(new UnifiedHeirarchicalObject("key1", "John Doe"));
        dataSlices.add(new UnifiedHeirarchicalObject("key2", "Employee_duty_43"));
        dataSlices.add(new UnifiedHeirarchicalObject("key4", "UPPERCASE"));
        
        // Apply the masking strategy
        noiseMask.mask(dataSlices);
        
        // Define expected masked values based on the replacement rules
        List<String> expectedValues = List.of(
            "J0hn D03",          // 'o' -> '0', 'e' -> '3'
            "3mpl0y33_duty_43",  // 'e' -> '3', 'o' -> '0', 'e' -> '3'
            "UPP3RC4S3"          // 'e' -> '3', 'a' -> '4', 'e' -> '3'
        );
        
        // Verify that each value matches the expected masked result
        for (int i = 0; i < dataSlices.size(); i++) {
            assertEquals(expectedValues.get(i), dataSlices.get(i).getValue(), 
                "Mismatch at index " + i + ": expected " + expectedValues.get(i) + 
                ", got " + dataSlices.get(i).getValue());
        }
    }
}