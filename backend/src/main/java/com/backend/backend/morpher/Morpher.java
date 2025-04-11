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

        List<MaskingStrategy> fullDataMasks = configLoader.getFullDataMaskStrategyList();
        fullDataMasks.forEach((maskingMethod) -> {
            try {
                List<UnifiedHeirarchicalObject> packagedData = dataFileLoader.getFullPackagedData();
                maskingMethod.mask(packagedData);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("UNABLE TO GET FULL PACKAGED DATA!!");
            }
        });
    }
}
