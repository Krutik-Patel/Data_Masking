package com.backend.backend.masks;

import java.util.Map;

public class MaskingFactory {
    public static MaskingStrategy createMask(String maskingMethod, Map<String, Object> parameters) {
        switch (maskingMethod) {
            case "HASH": return new HashMaskingStrategy(parameters);
            case "RANGESHIFT": return new RangeShiftStrategy(parameters);
            case "SUBSTITUTION": return new SubstitutionStrategy(parameters);
            case "KANONYMIZATION": return new KAnonymizationMaskingStrategy(parameters);

            default:
                throw new IllegalArgumentException("Unsupported masking method: " + maskingMethod);
        }
    }
}
