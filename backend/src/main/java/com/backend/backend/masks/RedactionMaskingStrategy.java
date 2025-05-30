package com.backend.backend.masks;

import java.util.List;
import java.util.Map;

import com.backend.backend.utils.UnifiedHeirarchicalObject;

public class RedactionMaskingStrategy implements MaskingStrategy {
    private Map<String, Object> parameters;
    public RedactionMaskingStrategy(Map<String, Object> params) {
        this.parameters = params;
    } 

    public void mask(List<UnifiedHeirarchicalObject> dataSlices) {
        dataSlices.forEach((row) -> {
            row.setValue(this.maskValue(row.getValue()));
        });
    }

    private String maskValue(String value) {
        return "REDACTED";
    }

    @Override
    public Map<String, Object> getParameters() { return this.parameters; }
}
