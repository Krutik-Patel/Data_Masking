package com.backend.backend.masks;

import java.util.List;
import java.util.Map;
import com.backend.backend.utils.UnifiedHeirarchicalObject;

public class NoiseInjectionStrategy implements MaskingStrategy {
    // Constructor: No parameters needed, so params is unused
    public NoiseInjectionStrategy(Map<String, Object> params) {
        // No initialization required
    }

    // Mask method: Applies noise injection to each string value in dataSlices
    public void mask(List<UnifiedHeirarchicalObject> dataSlices) {
        for (UnifiedHeirarchicalObject row : dataSlices) {
            String originalValue = row.getValue();
            String maskedValue = injectNoise(originalValue);
            row.setValue(maskedValue);
        }
    }

    // Helper method: Injects noise by replacing specific characters
    private String injectNoise(String value) {
        // Define replacement rules: 'o' -> '0', 'e' -> '3', 'a' -> '4', 'i' -> '1', 's' -> '5'
        String replacements = "o0e3a4i1s5";
        StringBuilder masked = new StringBuilder();

        // Process each character in the input string
        for (char c : value.toCharArray()) {
            char lowerC = c;
            int index = "oeais".indexOf(lowerC); // Check if the character is in the replacement set
            if (index != -1 && index < replacements.length() / 2) {
                // Replace with the corresponding number
                char replacement = replacements.charAt(index * 2 + 1);
                // Preserve original case if applicable (though numbers don't have case, apply as-is)
                masked.append(replacement);
            } else {
                masked.append(c); // Keep the original character if no replacement
            }
        }
        return masked.toString();
    }
}