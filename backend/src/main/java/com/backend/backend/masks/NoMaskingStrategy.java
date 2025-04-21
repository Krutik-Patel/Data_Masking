package com.backend.backend.masks;

import java.util.List;
import java.util.Map;
import com.backend.backend.utils.UnifiedHeirarchicalObject;

public class NoMaskingStrategy implements MaskingStrategy {
    private Map<String, Object> parameters;

    // Constructor: Store parameters (if provided)
    public NoMaskingStrategy(Map<String, Object> params) {
        this.parameters = params;
    }

    // Mask method: No changes are made to the data
    @Override
    public void mask(List<UnifiedHeirarchicalObject> dataSlices) {
        // No operation needed; data remains unchanged
    }

    @Override
    public Map<String, Object> getParameters() {
        return this.parameters;
    }
}