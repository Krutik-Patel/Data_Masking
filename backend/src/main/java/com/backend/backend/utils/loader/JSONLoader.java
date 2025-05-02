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

        // Assuming only one top-level key like { "main": { ... } }
        if (rootNode.size() != 1) {
            throw new IllegalArgumentException("Expected exactly one top-level element in JSON");
        }

        String firstKey = rootNode.fieldNames().next(); // "main"
        JsonNode mainNode = rootNode.get(firstKey);

        // Pass that node and its actual key to convertNode
        return convertNode(mainNode, firstKey, "");
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
                        if (child.isArray()) {
                            for(JsonNode arrayItem : child) {
                                UnifiedHeirarchicalObject arrayChildNode = convertNode(arrayItem, field, newXpath);
                                node.addChild(arrayChildNode);
                            }
                        } else {
                            UnifiedHeirarchicalObject childNode = convertNode(child, field, newXpath);
                            node.addChild(childNode);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
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

// else if (jsonNode.isArray()) {
// node = new UnifiedHeirarchicalObject(key, null);
// String newXpath = xPath + "/" + key;
// node.setXpath(newXpath);

// for (JsonNode child : jsonNode) {
// UnifiedHeirarchicalObject childNode = convertNode(child, "", newXpath);

// if (childNode.getChildren() != null) {
// for (UnifiedHeirarchicalObject grandchild : childNode.getChildren()) {
// node.addChild(grandchild); // Flatten the array items into the parent node
// }
// }
// }
// }

// else if (jsonNode.isArray()) {
// for (JsonNode child : jsonNode) {
// UnifiedHeirarchicalObject childNode = convertNode(child, key, xPath);
// node.addChild(childNode);
// }
// }


// else if (jsonNode.isArray()) {
//     UnifiedHeirarchicalObject node2 = new UnifiedHeirarchicalObject(key, null);
//     String newXpath2 = xPath + "/" + key;
//     node2.setXpath(newXpath2);

//     for (JsonNode child : jsonNode) {
//         UnifiedHeirarchicalObject childNode = convertNode(child, "", newXpath2);

//         if (childNode.getChildren() != null) {
//             for (UnifiedHeirarchicalObject grandchild : childNode.getChildren()) {
//                 node.addChild(grandchild); // Flatten the array items into the parent node
//             }
//         }
//     }
// }