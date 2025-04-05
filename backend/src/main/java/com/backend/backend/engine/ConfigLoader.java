package com.backend.backend.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.backend.backend.utils.loader.DataLoader;
import com.backend.backend.masks.MaskingFactory;
import com.backend.backend.masks.MaskingStrategy;
import com.backend.backend.utils.UnifiedHeirarchicalObject;

public class ConfigLoader {
    Map<String, MaskingStrategy> maskStrategyMap;
    public ConfigLoader() {
        this.maskStrategyMap = new HashMap<>();
    }

    public void parse(MultipartFile config) throws Exception {
        UnifiedHeirarchicalObject configTree = new DataLoader().loadContent(config);
        // first level is fields_info, children are the different fields
        List<UnifiedHeirarchicalObject> fields = configTree.getChildren();
        fields.forEach((field) -> {
            mapConfigToMask(field);
        });
    }

    private void mapConfigToMask(UnifiedHeirarchicalObject field) {
        // String fieldName = field.getNthChild(0).getValue();
        // String dataType = field.getNthChild(2).getValue();

        String fieldXPath = field.getNthChild(1).getValue();
        String maskingMethod = field.getNthChild(5).getNthChild(0).getValue();
        List<UnifiedHeirarchicalObject> mask_params = field.getNthChild(5).getNthChild(1).getChildren();
        Map<String, Object> parameters = new HashMap<>();
        for (UnifiedHeirarchicalObject param: mask_params) {
            parameters.put(param.getKey(), param.getValue());
        }
        MaskingStrategy mask_instance = MaskingFactory.createMask(maskingMethod, parameters);
        this.maskStrategyMap.put(fieldXPath, mask_instance);
    } 

    public Map<String, MaskingStrategy> getMaskStrategyMap() {
        return this.maskStrategyMap;
    }
}

// assumptions: config schema v1.1 is taken for this