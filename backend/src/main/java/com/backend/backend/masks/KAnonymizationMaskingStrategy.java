package com.backend.backend.masks;

import com.backend.backend.utils.UnifiedHeirarchicalObject;
import java.util.List;
import java.util.Map;

public class KAnonymizationMaskingStrategy implements MaskingStrategy {    
    private String algorithm;
    public KAnonymizationMaskingStrategy(Map<String, Object> params) {
        this.algorithm = (String) params.get("algorithm");
    }
    public void mask(List<UnifiedHeirarchicalObject> dataSlices) {
        throw new UnsupportedOperationException("Unimplemented Masking method: Kanonymization...");

    }
}
