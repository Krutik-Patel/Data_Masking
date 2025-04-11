package com.backend.backend.masks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.backend.backend.utils.UnifiedHeirarchicalObject;

public class NoiseInjectionStrategy implements MaskingStrategy {

    private static final Map<Character, Character> replacementMap = new HashMap<>();

    static {
        String charsToReplace = "oeaisOEAIS";
        String replacements = "0314503145";

        for (int i = 0; i < charsToReplace.length(); i++) {
            replacementMap.put(charsToReplace.charAt(i), replacements.charAt(i));
        }
    }

    public NoiseInjectionStrategy(Map<String, Object> params) {
        // No initialization required
    }

    @Override
    public void mask(List<UnifiedHeirarchicalObject> dataSlices) {
        for (UnifiedHeirarchicalObject row : dataSlices) {
            String originalValue = row.getValue();
            String maskedValue = injectNoise(originalValue);
            row.setValue(maskedValue);
        }
    }

    private String injectNoise(String value) {
        StringBuilder masked = new StringBuilder();

        for (char c : value.toCharArray()) {
            masked.append(replacementMap.getOrDefault(c, c));
        }

        return masked.toString();
    }
}
