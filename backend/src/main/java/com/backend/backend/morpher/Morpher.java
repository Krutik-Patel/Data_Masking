package com.backend.backend.morpher;

import com.backend.backend.engine.ConfigLoader;
import com.backend.backend.engine.DataFileLoader;

public class Morpher {

    private final ReferentialMaskingExecutor executor = new ReferentialMaskingExecutor();

    public void executeWaterFallOperations(ConfigLoader configLoader, DataFileLoader dataFileLoader) {
        executor.execute(
                configLoader.getMaskStrategyMap(),
                configLoader.getForeignKeyMap(),
                dataFileLoader);
    }
}