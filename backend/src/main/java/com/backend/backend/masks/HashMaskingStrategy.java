package com.backend.backend.masks;

import java.util.List;
import java.util.Map;

import com.backend.backend.utils.UnifiedHeirarchicalObject;

public class HashMaskingStrategy implements MaskingStrategy {
    private String algorithm;
    public HashMaskingStrategy(Map<String, Object> params) {
        this.algorithm = (String) params.get("algorithm");
    }
    @Override
    public void mask(List<UnifiedHeirarchicalObject> dataSlices) {
        throw new UnsupportedOperationException("Unimplemented method 'mask'");
    }
}
