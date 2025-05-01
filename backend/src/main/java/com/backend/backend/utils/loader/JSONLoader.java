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
        UnifiedHeirarchicalObject object = convertNode(rootNode, "root", "");
        return object;
    }

    private UnifiedHeirarchicalObject convertNode(JsonNode jsonNode, String key, String xPath) throws Exception {
        UnifiedHeirarchicalObject node;
        if (jsonNode.isContainerNode()) {
            node = new UnifiedHeirarchicalObject(key, null);
            String newXpath = xPath + "/" + key;
            node.setXpath(newXpath);
            if (jsonNode.isObject()) {
                jsonNode.fieldNames().forEachRemaining(field -> {
                    try {
                        JsonNode child = jsonNode.get(field);
                        UnifiedHeirarchicalObject childNode = convertNode(child, field, newXpath);
                        node.addChild(childNode);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            } else if (jsonNode.isArray()) {
                for (JsonNode child : jsonNode) {
                    UnifiedHeirarchicalObject childNode = convertNode(child, key, newXpath);
                    node.addChild(childNode);
                }
            }
        } else {
            String textValue = jsonNode.asText();
            String newXpath = xPath + "/" + key;
            node = new UnifiedHeirarchicalObject(key, textValue.isEmpty() ? null : textValue);
            node.setXpath(newXpath);
        }

        return node;
    }
}
