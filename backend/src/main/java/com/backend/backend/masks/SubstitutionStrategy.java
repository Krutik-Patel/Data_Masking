package com.backend.backend.masks;

import java.util.List;
import java.util.Map;

import com.backend.backend.utils.UnifiedHeirarchicalObject;

public class SubstitutionStrategy implements MaskingStrategy {
    private Map<String, Object> parameters;
    public SubstitutionStrategy(Map<String, Object> params) {
        this.parameters = params;
    }

    @Override
    public void mask(List<UnifiedHeirarchicalObject> dataSlices) {
        dataSlices.forEach((row) -> {
            row.setValue(this.maskValue(row.getValue()));
        });
    }

    private String maskValue(String value) {
        String prefix = value.substring(0, value.length() - 4);
        return prefix + "XXXX";
    }

    @Override
    public Map<String, Object> getParameters() { return this.parameters; }
}
