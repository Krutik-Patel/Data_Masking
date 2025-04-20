package com.backend.backend.morpher;

import com.backend.backend.engine.ConfigLoader;
import com.backend.backend.engine.DataFileLoader;

public class Morpher {

    private final ReferentialMaskingExecutor referentialExecutor = new ReferentialMaskingExecutor();
    private final FullDataMaskingExecutor fullDataExecutor = new FullDataMaskingExecutor();

    public void executeOperations(ConfigLoader configLoader, DataFileLoader dataFileLoader) {
        executeWaterFallOperations(configLoader, dataFileLoader);
        executeFullDataOperations(configLoader, dataFileLoader);
    }

    public void executeWaterFallOperations(ConfigLoader configLoader, DataFileLoader dataFileLoader) {
        referentialExecutor.execute(
                configLoader.getMaskStrategyMap(),
                configLoader.getForeignKeyMap(),
                dataFileLoader);
    }

    public void executeFullDataOperations(ConfigLoader ConfigLoader, DataFileLoader dataFileLoader) {
        fullDataExecutor.execute(ConfigLoader, dataFileLoader);
    }
}
