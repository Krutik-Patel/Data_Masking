package com.backend.backend.masks;

import com.backend.backend.utils.UnifiedHeirarchicalObject;
import java.util.*;
import java.util.stream.Collectors;

public class KAnonymizationMaskingStrategy implements MaskingStrategy {

    private int k;
    private List<String> quasiIdentifiers;

    public KAnonymizationMaskingStrategy(Map<String, Object> params) {
        this.k = Integer.parseInt(params.get("k").toString());
        this.quasiIdentifiers = (List<String>) params.get("quasi_identifiers");
    }

    @Override
    public void mask(List<UnifiedHeirarchicalObject> dataSlices) {
        if (dataSlices == null || dataSlices.isEmpty()) return;

        // Grouping by quasi-identifier values
        Map<String, List<UnifiedHeirarchicalObject>> groups = new HashMap<>();

        for (UnifiedHeirarchicalObject obj : dataSlices) {
            String key = quasiIdentifiers.stream()
                    .map(xpath -> String.valueOf(obj.getValueByXPath(xpath)))
                    .collect(Collectors.joining("|"));
            groups.computeIfAbsent(key, x -> new ArrayList<>()).add(obj);
        }

        // Apply masking on groups with size < k
        for (Map.Entry<String, List<UnifiedHeirarchicalObject>> entry : groups.entrySet()) {
            if (entry.getValue().size() < k) {
                for (UnifiedHeirarchicalObject obj : entry.getValue()) {
                    for (String xpath : quasiIdentifiers) {
                        obj.setValueByXPath(xpath, "*"); // Assuming this function exists
                    }
                }
            }
        }
    }
}
