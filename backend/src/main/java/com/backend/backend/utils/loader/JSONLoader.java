package com.backend.backend.utils.loader;

import com.backend.backend.utils.UnifiedHeirarchicalObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.multipart.MultipartFile;

public class JSONLoader implements DataLoaderStrategy {
    @Override
    public UnifiedHeirarchicalObject parseFile(MultipartFile file) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(file.getInputStream());
        UnifiedHeirarchicalObject object = convertNode(rootNode, "root");
        return object;
    }

    private UnifiedHeirarchicalObject convertNode(JsonNode jsonNode, String key) {
        UnifiedHeirarchicalObject node;
        if (jsonNode.isContainerNode()) {
            node = new UnifiedHeirarchicalObject(key, null);
            if (jsonNode.isObject()) {
                jsonNode.fieldNames().forEachRemaining(field -> {
                    JsonNode child = jsonNode.get(field);
                    node.addChild(convertNode(child, field));
                });
            } else if (jsonNode.isArray()) {
                for (JsonNode child : jsonNode) {
                    node.addChild(convertNode(child, key));
                } 
            }
        } else {
            String textValue = jsonNode.asText();
            node = new UnifiedHeirarchicalObject(key, textValue.isEmpty() ? null : textValue);
        }

        return node;
    }
}
