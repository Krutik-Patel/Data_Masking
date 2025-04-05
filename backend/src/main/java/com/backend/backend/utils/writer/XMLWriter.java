package com.backend.backend.utils.writer;

import java.util.List;

import com.backend.backend.utils.UnifiedHeirarchicalObject;

public class XMLWriter implements DataWriterStrategy {
    public String writeToString(UnifiedHeirarchicalObject object) {
        String content = readTree(object, 0);
        return content;
    }   

    private String readTree(UnifiedHeirarchicalObject object, int level) {
        if (!object.hasChildren()) {
            return "\t".repeat(level) + "<" + object.getKey() + ">" + object.getValue() + "</" + object.getKey() + ">\n";
        } else {
            List<UnifiedHeirarchicalObject> children = object.getChildren();
            String tree = "\t".repeat(level) + "<" + object.getKey() + ">\n";
            for (UnifiedHeirarchicalObject child: children) {
                tree += readTree(child, level + 1);
            }
            
            tree += "\t".repeat(level) + "</" + object.getKey() + ">\n";
            return tree;
        }
    }
}