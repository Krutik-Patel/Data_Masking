package com.backend.backend.masks;

import java.util.List;
import java.util.Map;
import com.backend.backend.utils.UnifiedHeirarchicalObject;

public class PartialMaskingStrategy implements MaskingStrategy {
    private int x;

    public PartialMaskingStrategy(Map<String, Object> params) {
        if (params != null && params.containsKey("x")) {
            Object xObj = params.get("x");
            if (xObj instanceof Integer) {
                this.x = (Integer) xObj;
            } else {
                throw new IllegalArgumentException("Parameter 'x' must be an integer");
            }
        } else {
            this.x = 4; // Default value if params is null or "x" is missing
        }
    }

    public void mask(List<UnifiedHeirarchicalObject> dataSlices) {
        for (UnifiedHeirarchicalObject row : dataSlices) {
            String originalValue = row.getValue();
            String maskedValue = maskValue(originalValue, x);
            row.setValue(maskedValue);
        }
    }

    private String maskValue(String value, int x) {
        int len = value.length();
        if (x <= 0) {
            return value; // No masking if x is 0 or negative
        } else if (x >= len) {
            return "X".repeat(len); // Mask entire string if x >= length
        } else {
            return "X".repeat(x) + value.substring(x); // Mask first x characters
        }
    }
}