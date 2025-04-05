package com.backend.backend.utils.writer;

import java.util.List;

import com.backend.backend.utils.UnifiedHeirarchicalObject;

public class JSONWriter implements DataWriterStrategy {
    public String writeToString(UnifiedHeirarchicalObject object) {
        String content = readTree(object, 0);
        return content;
    }
    
    private String readTree(UnifiedHeirarchicalObject object, int level) {
        throw new UnsupportedOperationException("Unimplemented class JSON Writer... Use XMLWriter instead!");
        // if (!object.hasChildren()) {
        //     return "\t".repeat(level) + object.getKey() + ": " + object.getValue() + ",\n";
        // } else {
        //     List<UnifiedHeirarchicalObject> children = object.getChildren();
        //     String tree = "";
        //     // for (UnifiedHeirarchicalObject child: children) {

        //     // }
        //     return tree;
        // }
        // return "INCOMPLETE IMPLEMENTATION OF JSON WRITER: PLEASE USE XMLWRITER INSTEAD";

    }
}
