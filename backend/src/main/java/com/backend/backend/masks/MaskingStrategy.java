package com.backend.backend.masks;

import com.backend.backend.utils.UnifiedHeirarchicalObject;
import java.util.List;
import java.util.Map;

public interface MaskingStrategy {
    public void mask(List<UnifiedHeirarchicalObject> dataSlices);
    public Map<String, Object> getParameters();
}
