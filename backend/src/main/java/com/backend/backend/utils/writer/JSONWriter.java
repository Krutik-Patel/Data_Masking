package com.backend.backend.utils.writer;

import com.backend.backend.utils.UnifiedHeirarchicalObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JSONWriter implements DataWriterStrategy {

    // @Override
    // public String writeToString(UnifiedHeirarchicalObject object) {
    // return readTree(object, 0);
    // }

    private void printTree(UnifiedHeirarchicalObject node, int level) {
        String indent = "  ".repeat(level);
        System.out.println(indent + "- " + node.getKey() +
                (node.getValue() != null ? ": " + node.getValue() : ""));

        if (node.getChildren() != null) {
            for (UnifiedHeirarchicalObject child : node.getChildren()) {
                printTree(child, level + 1);
            }
        }
    }

    @Override
    public String writeToString(UnifiedHeirarchicalObject object) {
        // printTree(object, 0);
        // return "{\n" + readTree(object, 1) + "\n}";

        return "{\n\t\"" + object.getKey() + "\": {\n" + readTree(object, 2) + "\n\t}\n}";

        // return readTree(object, 1);
    }

    /*
     * If you are on a node and
     * - node has no children then just create the string node-key : node-value
     * - node has children then
     * - group children by key
     * - and first for each children create the string of their tree recursively
     * 
     * Then its time to create the string for the current node.
     * 
     * - add { to json
     * - for each group
     * - if group has only one child then
     * - add "child-key" : "child-string" to json
     * - else if group has multiple children then
     * - add "group-key" : [ all the child-strings of the group ] to json
     * - add } to json
     */

    private String readTree2(UnifiedHeirarchicalObject object, int level) {
        StringBuilder json = new StringBuilder();
        String indent = "\t".repeat(level);

        if (!object.hasChildren()) {
            json.append(indent)
                    .append("\"").append(object.getKey()).append("\": ")
                    .append("\"").append(object.getValue()).append("\"");
        } else {
            // Group children by key
            Map<String, List<UnifiedHeirarchicalObject>> grouped = new LinkedHashMap<>();
            for (UnifiedHeirarchicalObject child : object.getChildren()) {
                grouped.computeIfAbsent(child.getKey(), k -> new ArrayList<>()).add(child);
            }

            json.append(indent).append("\"").append(object.getKey()).append("\": {\n");

            int groupCount = 0;
            int totalGroups = grouped.size();

            for (Map.Entry<String, List<UnifiedHeirarchicalObject>> entry : grouped.entrySet()) {
                List<UnifiedHeirarchicalObject> children = entry.getValue();
                String key = entry.getKey();

                json.append("\t".repeat(level + 1)).append("\"").append(key).append("\": ");

                if (children.size() == 1) {
                    UnifiedHeirarchicalObject child = children.get(0);

                    if (child.hasChildren()) {
                        json.append("{\n")
                                .append(readTree(child, level + 2)).append("\n")
                                .append("\t".repeat(level + 1)).append("}");
                    } else {
                        json.append("\"").append(child.getValue()).append("\"");
                    }
                } else {
                    // List of objects
                    json.append("[\n");
                    for (int i = 0; i < children.size(); i++) {
                        UnifiedHeirarchicalObject child = children.get(i);
                        json.append("\t".repeat(level + 2)).append("{\n")
                                .append(readTree(child, level + 3)).append("\n")
                                .append("\t".repeat(level + 2)).append("}");
                        if (i < children.size() - 1) {
                            json.append(",");
                        }
                        json.append("\n");
                    }
                    json.append("\t".repeat(level + 1)).append("]");
                }

                if (++groupCount < totalGroups) {
                    json.append(",");
                }
                json.append("\n");
            }

            json.append(indent).append("}");
        }

        return json.toString();
    }

    private String readTree(UnifiedHeirarchicalObject node, int level) {
        String indent = "\t".repeat(level);
        StringBuilder json = new StringBuilder();

        // Leaf node: just key : value
        if (!node.hasChildren()) {
            json.append(indent)
                    .append("\"").append(node.getKey()).append("\": ")
                    .append("\"").append(node.getValue()).append("\"");
            return json.toString();
        }

        // Node has children: group them by key
        Map<String, List<UnifiedHeirarchicalObject>> grouped = new LinkedHashMap<>();
        for (UnifiedHeirarchicalObject child : node.getChildren()) {
            grouped.computeIfAbsent(child.getKey(), k -> new ArrayList<>()).add(child);
        }

        // Build JSON object string
        // json.append(indent).append("\"").append(node.getKey()).append("\": {\n");

        int groupCount = 0;
        int totalGroups = grouped.size();

        for (Map.Entry<String, List<UnifiedHeirarchicalObject>> entry : grouped.entrySet()) {
            String childKey = entry.getKey();
            List<UnifiedHeirarchicalObject> group = entry.getValue();

            json.append("\t".repeat(level + 1))
                    .append("\"").append(childKey).append("\": ");

            if (group.size() == 1) {
                UnifiedHeirarchicalObject onlyChild = group.get(0);
                if (onlyChild.hasChildren()) {
                    json.append("{\n")
                            .append(readTree(onlyChild, level + 2)).append("\n")
                            .append("\t".repeat(level + 1)).append("}");
                } else {
                    json.append("\"").append(onlyChild.getValue()).append("\"");
                }
            } else {
                json.append("[\n");
                for (int i = 0; i < group.size(); i++) {
                    UnifiedHeirarchicalObject child = group.get(i);
                    json.append("\t".repeat(level + 2)).append("{\n")
                            .append(readTree(child, level + 3)).append("\n")
                            .append("\t".repeat(level + 2)).append("}");
                    if (i < group.size() - 1) {
                        json.append(",");
                    }
                    json.append("\n");
                }
                json.append("\t".repeat(level + 1)).append("]");
            }

            if (++groupCount < totalGroups) {
                json.append(",");
            }
            json.append("\n");
        }

        // json.append(indent).append("}");

        return json.toString();
    }

   
   
}

