package com.backend.backend.masks;

import com.backend.backend.utils.UnifiedHeirarchicalObject;
import java.util.*;

public class LDiversityMaskingStrategy implements MaskingStrategy {

    private int l;
    private List<String> quasiIdentifiers;
    private String sensitiveAttributeXpath;

    public LDiversityMaskingStrategy(Map<String, Object> params) {
        this.l = Integer.parseInt(params.get("l").toString());
        this.quasiIdentifiers = (List<String>) params.get("quasi_identifiers");
        this.sensitiveAttributeXpath = params.get("sensitive_attribute").toString();
    }

    @Override
    public void mask(List<UnifiedHeirarchicalObject> dataSlices) {
        if (dataSlices == null || dataSlices.isEmpty()) return;

        Map<String, List<UnifiedHeirarchicalObject>> groups = new HashMap<>();

        // Group by Quasi-identifiers
        for (UnifiedHeirarchicalObject obj : dataSlices) {
            List<String> values = new ArrayList<>();
            for (String xpath : quasiIdentifiers) {
                UnifiedHeirarchicalObject childNode = obj.getChildByXpath(xpath);
                values.add(childNode != null ? childNode.getValue() : null);
            }
            String key = String.join("|", values);
            groups.computeIfAbsent(key, k -> new ArrayList<>()).add(obj);
        }

        // Mask sensitive attribute if l-diversity not met
        for (Map.Entry<String, List<UnifiedHeirarchicalObject>> entry : groups.entrySet()) {
            Set<String> sensitiveValues = new HashSet<>();
            for (UnifiedHeirarchicalObject obj : entry.getValue()) {
                UnifiedHeirarchicalObject sensitiveNode = obj.getChildByXpath(sensitiveAttributeXpath);
                if (sensitiveNode != null) {
                    sensitiveValues.add(sensitiveNode.getValue());
                }
            }

            if (sensitiveValues.size() < l) {
                for (UnifiedHeirarchicalObject obj : entry.getValue()) {
                    UnifiedHeirarchicalObject sensitiveNode = obj.getChildByXpath(sensitiveAttributeXpath);
                    if (sensitiveNode != null) {
                        sensitiveNode.setValue("*");
                    }
                }
            }
        }
    }
}
