// package com.backend.backend.utils.writer;

// import java.util.List;

// import com.backend.backend.utils.UnifiedHeirarchicalObject;

// public class JSONWriter implements DataWriterStrategy {
//     public String writeToString(UnifiedHeirarchicalObject object) {
//         String content = readTree(object, 0);
//         return content;
//     }
    
//     private String readTree(UnifiedHeirarchicalObject object, int level) {
//         throw new UnsupportedOperationException("Unimplemented class JSON Writer... Use XMLWriter instead!");
//         // if (!object.hasChildren()) {
//         //     return "\t".repeat(level) + object.getKey() + ": " + object.getValue() + ",\n";
//         // } else {
//         //     List<UnifiedHeirarchicalObject> children = object.getChildren();
//         //     String tree = "";
//         //     // for (UnifiedHeirarchicalObject child: children) {

//         //     // }
//         //     return tree;
//         // }
//         // return "INCOMPLETE IMPLEMENTATION OF JSON WRITER: PLEASE USE XMLWRITER INSTEAD";

//     }
// }




package com.backend.backend.utils.writer;

import com.backend.backend.utils.UnifiedHeirarchicalObject;
import java.util.List;

public class JSONWriter implements DataWriterStrategy {

    @Override
    public String writeToString(UnifiedHeirarchicalObject object) {
        return readTree(object, 0);
    }

    private String readTree(UnifiedHeirarchicalObject object, int level) {
        StringBuilder json = new StringBuilder();
        String indent = "\t".repeat(level);

        if (!object.hasChildren()) {
            // Leaf node
            json.append(indent)
                .append("\"").append(object.getKey()).append("\": ")
                .append("\"").append(object.getValue()).append("\"");
        } else {
            // Object node
            json.append(indent)
                .append("\"").append(object.getKey()).append("\": {\n");

            List<UnifiedHeirarchicalObject> children = object.getChildren();
            int size = children.size();
            for (int i = 0; i < size; i++) {
                json.append(readTree(children.get(i), level + 1));
                if (i != size - 1) {
                    json.append(",");
                }
                json.append("\n");
            }

            json.append(indent).append("}");
        }

        return json.toString();
    }
}




// java -jar target/backend-0.0.1-SNAPSHOT.jar --data /home/ricky/Desktop/sem8/DM/weekly_project/Data_Masking/example_config_and_engine_core_files/data_files/sample_data4.json --config /home/ricky/Desktop/sem8/DM/weekly_project/Data_Masking/example_config_and_engine_core_files/config_files/sample_config4.json --out masked_result.json