package com.backend.backend.masks;

import com.backend.backend.utils.UnifiedHeirarchicalObject;
import java.util.List;

public interface MaskingStrategy {
    public void mask(List<UnifiedHeirarchicalObject> dataSlices);
}
