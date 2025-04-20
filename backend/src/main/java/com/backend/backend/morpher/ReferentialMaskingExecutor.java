package com.backend.backend.morpher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.backend.backend.engine.DataFileLoader;
import com.backend.backend.masks.MaskingStrategy;
import com.backend.backend.utils.UnifiedHeirarchicalObject;

public class ReferentialMaskingExecutor {

    // This will be used to do masking when there are realtions between the fields
    // and the masking has to be done on all of them like a waterfall. Basically a
    // dfs on the relation tree.
    public void execute(Map<String, MaskingStrategy> maskStrategyMap,
            Map<String, List<String>> foreignKeyMap,
            DataFileLoader dataFileLoader) {

        maskStrategyMap.forEach((xPath, strategy) -> {
            Map<String, String> valueTransformationLog = new HashMap<>();

            dataFileLoader.getValueToDataMap(xPath).forEach((value, dataList) -> {
                maskFieldValue(xPath, value, strategy, dataFileLoader, foreignKeyMap);
                if (!valueTransformationLog.containsKey(value)) {
                    valueTransformationLog.put(value, dataList.get(0).getValue());
                }
            });

            applyValueTransformationLog(xPath, valueTransformationLog, dataFileLoader.getXPathValueToDataMap(),
                    foreignKeyMap);

        });
    }

    private void maskFieldValue(String xPath, String value, MaskingStrategy strategy,
            DataFileLoader dataFileLoader,
            Map<String, List<String>> foreignKeyMap) {

        List<UnifiedHeirarchicalObject> data = dataFileLoader.getNodesByXpathAndValue(xPath, value);
        strategy.mask(data);

        // Change the value in (xpath,value) -> Data Map.

        List<String> foreignXPaths = foreignKeyMap.get(xPath);
        if (foreignXPaths != null) {
            for (String foreignXPath : foreignXPaths) {
                maskFieldValue(foreignXPath, value, strategy, dataFileLoader, foreignKeyMap);
            }
        }
    }

    public void applyValueTransformationLog(
            String xPath,
            Map<String, String> valueTransformationLog,
            Map<String, Map<String, List<UnifiedHeirarchicalObject>>> xPathValueToDataMap,
            Map<String, List<String>> foreignKeyMap) {

        // For the given xpath, apply the valueTransformationLog
        Map<String, List<UnifiedHeirarchicalObject>> currentMap = xPathValueToDataMap.get(xPath);
        Map<String, List<UnifiedHeirarchicalObject>> updatedMap = new HashMap<>();

        currentMap.forEach((oldValue, dataList) -> {
            String newValue = valueTransformationLog.getOrDefault(oldValue, oldValue);
            updatedMap.computeIfAbsent(newValue, v -> new ArrayList<>()).addAll(dataList);
        });

        xPathValueToDataMap.put(xPath, updatedMap);

        List<String> foreignXPaths = foreignKeyMap.get(xPath);
        if (foreignXPaths != null) {
            for (String foreignXPath : foreignXPaths) {
                applyValueTransformationLog(foreignXPath, valueTransformationLog, xPathValueToDataMap,
                        foreignKeyMap);
            }
        }
    }

}
