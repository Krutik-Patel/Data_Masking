package com.backend.backend.masks;

import com.backend.backend.utils.UnifiedHeirarchicalObject;
import java.util.*;

public class KAnonymizationMaskingStrategy implements MaskingStrategy {

    private int k;
    private List<String> quasiIdentifiers;
    private Map<String, Object> parameters;

    public KAnonymizationMaskingStrategy(Map<String, Object> params) {
        this.parameters = params;
        this.k = Integer.parseInt((String) params.get("k"));
        this.quasiIdentifiers = (List<String>) params.get("quasi_identifiers");
    }

    @Override
    public void mask(List<UnifiedHeirarchicalObject> dataSlices) {
        if (dataSlices == null || dataSlices.isEmpty())
            return;

        // Group objects by quasi-identifier values extracted via XPaths
        Map<String, List<UnifiedHeirarchicalObject>> groups = new HashMap<>();

        for (UnifiedHeirarchicalObject obj : dataSlices) {
            // Build a key by collecting values for all provided XPaths
            List<String> values = new ArrayList<>();
            for (String xpath : quasiIdentifiers) {
                UnifiedHeirarchicalObject childNode = obj.getChildByXpath(xpath);
                // Use empty string if the node is missing or uninitialized
                values.add(childNode != null ? childNode.getValue() : null);
            }
            String key = String.join("|", values);
            groups.computeIfAbsent(key, keyVal -> new ArrayList<>()).add(obj);
        }

        // Mask values if the group size is smaller than k
        for (Map.Entry<String, List<UnifiedHeirarchicalObject>> entry : groups.entrySet()) {
            if (entry.getValue().size() < k) {
                for (UnifiedHeirarchicalObject obj : entry.getValue()) {
                    for (String xpath : quasiIdentifiers) {
                        UnifiedHeirarchicalObject childNode = obj.getChildByXpath(xpath);
                        if (childNode != null) {
                            childNode.setValue("*");
                        }
                    }
                }
            }
        }
    }

    @Override
    public Map<String, Object> getParameters() { return this.parameters; }
}
