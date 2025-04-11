package com.backend.backend.masks;

import java.util.Map;

public class MaskingFactory {
    public static MaskingStrategy createMask(String maskingMethod, Map<String, Object> parameters) {
        switch (maskingMethod) {
            case "HASH": return new HashMaskingStrategy(parameters);
            case "RANGESHIFT": return new RangeShiftStrategy(parameters);
            case "SUBSTITUTION": return new SubstitutionStrategy(parameters);
            case "REDACT": return new RedactionMaskingStrategy(parameters);
            case "EMAIL": return new EmailMaskingStrategy(parameters);
            case "KANONYMIZATION": return new KAnonymizationMaskingStrategy(parameters);
            case "PARTIAL_MASKING": return new PartialMaskingStrategy(parameters);
            case "RANDOMIZATION": return new RandomizationStrategy(parameters);
            case "BINNING": return new BinningStrategy(parameters);
            case "NOISE_INJECTION": return new NoiseInjectionStrategy(parameters);
            
            default:
                throw new IllegalArgumentException("Unsupported masking method: " + maskingMethod);
        }
    }
}
