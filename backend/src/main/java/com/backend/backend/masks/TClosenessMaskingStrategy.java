package com.backend.backend.masks;

import com.backend.backend.utils.UnifiedHeirarchicalObject;
import java.util.*;

public class TClosenessMaskingStrategy implements MaskingStrategy {

    private double t;
    private List<String> quasiIdentifiers;
    private String sensitiveAttributeXpath;
    private Map<String, Object> parameters;

    public TClosenessMaskingStrategy(Map<String, Object> params) {
        this.parameters = params;
        this.t = Double.parseDouble((String) params.get("t"));
        this.quasiIdentifiers = (List<String>) params.get("quasi_identifiers");
        List<String> sensitiveXpaths = (List<String>) params.get("sensitive_identifiers");
        this.sensitiveAttributeXpath = sensitiveXpaths.get(0);
    }

    @Override
    public void mask(List<UnifiedHeirarchicalObject> dataSlices) {
        if (dataSlices == null || dataSlices.isEmpty()) {
            return;
        }

        // Compute overall distribution of sensitive attribute
        Map<String, Double> overallDistribution = computeOverallDistribution(dataSlices);

        // Group by Quasi-identifiers
        Map<String, List<UnifiedHeirarchicalObject>> groups = new HashMap<>();
        for (UnifiedHeirarchicalObject obj : dataSlices) {
            List<String> values = new ArrayList<>();
            for (String xpath : quasiIdentifiers) {
                UnifiedHeirarchicalObject childNode = obj.getChildByXpath(xpath);
                values.add(childNode != null ? childNode.getValue() : null);
            }
            String key = String.join("|", values);
            groups.computeIfAbsent(key, k -> new ArrayList<>()).add(obj);
        }

        // Check t-closeness for each group and mask if violated
        for (Map.Entry<String, List<UnifiedHeirarchicalObject>> entry : groups.entrySet()) {
            Map<String, Double> groupDistribution = computeGroupDistribution(entry.getValue());
            double distance = computeDistributionDistance(overallDistribution, groupDistribution);

            if (distance > t) {
                for (UnifiedHeirarchicalObject obj : entry.getValue()) {
                    UnifiedHeirarchicalObject sensitiveNode = obj.getChildByXpath(sensitiveAttributeXpath);
                    if (sensitiveNode != null) {
                        sensitiveNode.setValue("*");
                    }
                }
            }
        }
    }

    // Compute the overall distribution of the sensitive attribute
    private Map<String, Double> computeOverallDistribution(List<UnifiedHeirarchicalObject> dataSlices) {
        Map<String, Integer> counts = new HashMap<>();
        int total = 0;

        for (UnifiedHeirarchicalObject obj : dataSlices) {
            UnifiedHeirarchicalObject sensitiveNode = obj.getChildByXpath(sensitiveAttributeXpath);
            if (sensitiveNode != null) {
                String value = sensitiveNode.getValue();
                counts.put(value, counts.getOrDefault(value, 0) + 1);
                total++;
            }
        }

        Map<String, Double> distribution = new HashMap<>();
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            distribution.put(entry.getKey(), (double) entry.getValue() / total);
        }
        return distribution;
    }

    // Compute the distribution of the sensitive attribute in a group
    private Map<String, Double> computeGroupDistribution(List<UnifiedHeirarchicalObject> group) {
        Map<String, Integer> counts = new HashMap<>();
        int total = 0;

        for (UnifiedHeirarchicalObject obj : group) {
            UnifiedHeirarchicalObject sensitiveNode = obj.getChildByXpath(sensitiveAttributeXpath);
            if (sensitiveNode != null) {
                String value = sensitiveNode.getValue();
                counts.put(value, counts.getOrDefault(value, 0) + 1);
                total++;
            }
        }

        Map<String, Double> distribution = new HashMap<>();
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            distribution.put(entry.getKey(), (double) entry.getValue() / total);
        }
        return distribution;
    }

    // Compute the distance between two distributions (simplified: max difference)
    private double computeDistributionDistance(Map<String, Double> overall, Map<String, Double> group) {
        double maxDiff = 0.0;
        Set<String> allValues = new HashSet<>();
        allValues.addAll(overall.keySet());
        allValues.addAll(group.keySet());

        for (String value : allValues) {
            double p1 = overall.getOrDefault(value, 0.0);
            double p2 = group.getOrDefault(value, 0.0);
            maxDiff = Math.max(maxDiff, Math.abs(p1 - p2));
        }
        return maxDiff;
    }

    @Override
    public Map<String, Object> getParameters() {
        return this.parameters;
    }
}