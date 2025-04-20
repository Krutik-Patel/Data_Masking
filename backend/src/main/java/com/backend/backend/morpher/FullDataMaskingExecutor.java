package com.backend.backend.morpher;

import java.util.List;

import org.springframework.boot.context.config.ConfigDataLoader;

import com.backend.backend.engine.ConfigLoader;
import com.backend.backend.engine.DataFileLoader;
import com.backend.backend.masks.MaskingStrategy;
import com.backend.backend.utils.UnifiedHeirarchicalObject;
import com.backend.backend.utils.loader.DataLoader;

public class FullDataMaskingExecutor {

    public void execute(ConfigLoader configLoader, DataFileLoader dataFileLoader) {
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