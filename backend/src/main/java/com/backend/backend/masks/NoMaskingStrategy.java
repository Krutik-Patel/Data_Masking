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

    @Override
    public void mask(List<UnifiedHeirarchicalObject> dataSlices) {
        System.out.println("Unimplemented mask method of NONE Strategy");
    }

    @Override
    public Map<String, Object> getParameters() {
        return this.parameters;
    }
}