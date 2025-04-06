package com.backend.backend.morpher;

import com.backend.backend.engine.ConfigLoader;
import com.backend.backend.engine.DataFileLoader;
import com.backend.backend.masks.MaskingStrategy;
import com.backend.backend.utils.*;
import java.util.*;

public class Morpher {
    public void executeOperations(ConfigLoader configLoader, DataFileLoader dataFileLoader) {
        Map<String, MaskingStrategy> maskStrateMap = configLoader.getMaskStrategyMap();
        maskStrateMap.forEach((xPath, maskingMethod) -> {
            List<UnifiedHeirarchicalObject> data = dataFileLoader.getNodesByXPath(xPath);
            maskingMethod.mask(data);
        });
    }
}
