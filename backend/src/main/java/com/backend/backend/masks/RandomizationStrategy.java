package com.backend.backend.masks;

import java.util.List;
import java.util.Map;
import java.util.Random;
import com.backend.backend.utils.UnifiedHeirarchicalObject;

public class RandomizationStrategy implements MaskingStrategy {
    private static final String ALPHANUMERIC_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private Random random;
    private Map<String, Object> parameters;

    // Constructor: No parameters required
    public RandomizationStrategy(Map<String, Object> params) {
        this.parameters = params;
        this.random = new Random();
    }

    // Mask method: Randomizes the value of each object
    public void mask(List<UnifiedHeirarchicalObject> dataSlices) {
        for (UnifiedHeirarchicalObject row : dataSlices) {
            String originalValue = row.getValue();
            String randomizedValue = randomizeValue(originalValue);
            row.setValue(randomizedValue);
        }
    }

    // Helper method: Generates a random alphanumeric string of the same length as the input
    private String randomizeValue(String value) {
        int length = value.length();
        StringBuilder randomized = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(ALPHANUMERIC_CHARS.length());
            randomized.append(ALPHANUMERIC_CHARS.charAt(index));
        }
        return randomized.toString();
    }

    @Override
    public Map<String, Object> getParameters() { return this.parameters; }
}