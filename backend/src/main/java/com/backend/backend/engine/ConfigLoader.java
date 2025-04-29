package com.backend.backend.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.backend.backend.utils.loader.DataLoader;
import com.backend.backend.utils.writer.DataWriter;
import com.backend.backend.utils.writer.DataWriter.DataFormat;
import com.backend.backend.masks.MaskingFactory;
import com.backend.backend.masks.MaskingStrategy;
import com.backend.backend.utils.UnifiedHeirarchicalObject;

public class ConfigLoader {
    Map<String, MaskingStrategy> maskStrategyMap;
    List<MaskingStrategy> fulldataMasksList;

    // Will store the mapping of primary key to the list of fields which are
    // foreign key for the current field
    Map<String, List<String>> foreignKeyMap;

    private UnifiedHeirarchicalObject configTree;

    public ConfigLoader() {
        this.maskStrategyMap = new HashMap<>();
        this.foreignKeyMap = new HashMap<>();
        this.fulldataMasksList = new ArrayList<>();
    }

    public void parse(MultipartFile config) throws Exception {
        UnifiedHeirarchicalObject configTree = new DataLoader().loadContent(config);
        this.configTree = configTree;
        // first level is fields_info, children are the different fields
        List<UnifiedHeirarchicalObject> fields = this.configTree.getChildren();
        fields.forEach((field) -> {
            if (field.getKey().equals("field")) {
                mapConfigToMask(field);
            } else {
                mapFullDataConfigMask(field);
            }
        });
    }

    private void mapConfigToMask(UnifiedHeirarchicalObject field) {
        // String fieldName = field.getNthChild(0).getValue();
        // String dataType = field.getNthChild(2).getValue();

        String fieldXPath = field.getChildByName("field_xPath").getValue();
        String maskingMethod = field.getChildByName("morphing_method").getChildByName("method_name").getValue();
        List<UnifiedHeirarchicalObject> mask_params = field.getChildByName("morphing_method")
                .getChildByName("method_parameters").getChildren();
        Map<String, Object> parameters = new HashMap<>();
        for (UnifiedHeirarchicalObject param : mask_params) {
            parameters.put(param.getKey(), param.getValue());
        }

        // CODE FOR WHEN USING REFERS_TO
        // check if the there are any fields in refers_to
        if (field.getChildByName("refersTo").hasChildren()) {
            List<UnifiedHeirarchicalObject> refersTo = field.getChildByName("refersTo").getChildren();
            for (UnifiedHeirarchicalObject ref : refersTo) {
                String refXPath = ref.getValue();
                if (!this.foreignKeyMap.containsKey(refXPath)) {
                    this.foreignKeyMap.put(refXPath, new ArrayList<>());
                }
                this.foreignKeyMap.get(refXPath).add(fieldXPath);
            }
        }

        MaskingStrategy mask_instance = MaskingFactory.createMask(maskingMethod, parameters);
        this.maskStrategyMap.put(fieldXPath, mask_instance);
    }

    private void mapFullDataConfigMask(UnifiedHeirarchicalObject field) {
        String algorithm = field.getChildByName("algorithm").getValue();
        List<UnifiedHeirarchicalObject> identifiers = field.getChildByName("identifiers").getChildren();
        Map<String, Object> parameters = new HashMap<>();
        identifiers.forEach((identifier) -> {
            List<String> id_xpaths = new ArrayList<>();
            for (UnifiedHeirarchicalObject child : identifier.getChildren()) {
                id_xpaths.add(child.getValue());
            }
            parameters.put(identifier.getKey(), id_xpaths);
        });
        List<UnifiedHeirarchicalObject> mask_params = field.getChildByName("parameters").getChildren();
        for (UnifiedHeirarchicalObject param : mask_params) {
            parameters.put(param.getKey(), param.getValue());
        }
        MaskingStrategy mask_instance = MaskingFactory.createMask(algorithm, parameters);
        this.fulldataMasksList.add(mask_instance);
    }

    public List<MaskingStrategy> getFullDataMaskStrategyList() {
        return this.fulldataMasksList;
    }

    public Map<String, MaskingStrategy> getMaskStrategyMap() {
        return this.maskStrategyMap;
    }

    public Map<String, List<String>> getForeignKeyMap() {
        return this.foreignKeyMap;
    }

    public String stringifyConfig() throws Exception {
        String configString = new DataWriter().writeToString(this.configTree, DataFormat.XML);
        return configString;
    }
}

// assumptions: config schema v1.1 is taken for this