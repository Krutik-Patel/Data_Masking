// package com.backend.backend.masks;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertNotEquals;

// import java.util.ArrayList;
// import java.util.List;
// import org.junit.jupiter.api.Test;
// import com.backend.backend.utils.UnifiedHeirarchicalObject;

// public class RandomizationStrategyTest {
//     @Test
//     public void testRandomizationStrategy() {
//         // Initialize the strategy with no parameters (null)
//         MaskingStrategy randomMask = new RandomizationStrategy(null);
        
//         // Create sample data
//         List<UnifiedHeirarchicalObject> dataSlices = new ArrayList<>();
//         dataSlices.add(new UnifiedHeirarchicalObject("key1", "12345"));         // 5-digit number
//         dataSlices.add(new UnifiedHeirarchicalObject("key2", "John"));          // 4-letter name
//         dataSlices.add(new UnifiedHeirarchicalObject("key3", "Employee_duty")); // Mixed string
//         dataSlices.add(new UnifiedHeirarchicalObject("key4", "Test123"));       // Alphanumeric
        
//         // Store original values
//         List<String> originalValues = new ArrayList<>();
//         for (UnifiedHeirarchicalObject obj : dataSlices) {
//             originalValues.add(obj.getValue());
//         }
        
//         // Apply randomization
//         randomMask.mask(dataSlices);
        
//         // Verify results
//         for (int i = 0; i < dataSlices.size(); i++) {
//             String original = originalValues.get(i);
//             String randomized = dataSlices.get(i).getValue();
            
//             // Check length preservation
//             assertEquals(original.length(), randomized.length(), 
//                 "Length should match for " + original);
            
//             // Check that the value has changed
//             assertNotEquals(original, randomized, 
//                 "Randomized value should differ from " + original);
            
//             // Check that the output is alphanumeric
//             assertTrue(isAlphanumeric(randomized), 
//                 "Output should be alphanumeric for " + original);
//         }
//     }
    
//     // Helper method to verify alphanumeric content
//     private boolean isAlphanumeric(String str) {
//         return str.matches("[A-Za abroad-z0-9]+");
//     }
// }