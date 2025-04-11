package com.backend.backend.engine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.rmi.server.ExportException;
import java.util.ArrayList;

import org.springframework.web.multipart.MultipartFile;

import com.backend.backend.utils.UnifiedHeirarchicalObject;
import com.backend.backend.utils.loader.DataLoader;
import com.backend.backend.utils.writer.DataWriter;
import com.backend.backend.utils.writer.DataWriter.DataFormat;

public class DataFileLoader {
    private UnifiedHeirarchicalObject dataFile;
    private Map<String, List<UnifiedHeirarchicalObject>> xPathToData;
    private List<UnifiedHeirarchicalObject> packagedData;

    public DataFileLoader() {
        this.xPathToData = new HashMap<>();
    }

    public void parse(MultipartFile data) throws Exception {
        UnifiedHeirarchicalObject dataFile = new DataLoader().loadContent(data);
        this.dataFile = dataFile;
        createXPathToDataReferenceMapping(this.dataFile, "");
    }

    private void createXPathToDataReferenceMapping(UnifiedHeirarchicalObject node, String currXpath) {
        if (!node.hasChildren()) {
            String xPath = currXpath + "/" + node.getKey();
            xPathToData.computeIfAbsent(xPath, k -> new ArrayList<>()).add(node);
        } else {
            String nextXpath = currXpath + "/" + node.getKey();
            List<UnifiedHeirarchicalObject> children = node.getChildren();
            children.forEach((child) -> createXPathToDataReferenceMapping(child, nextXpath));
        }
    }

    public List<UnifiedHeirarchicalObject> getFullPackagedData() throws Exception {
        if (this.packagedData == null) {
            this.packagedData = packageDataAsRecords();
        }
        return this.packagedData;
    }

    private List<UnifiedHeirarchicalObject> packageDataAsRecords() throws Exception {
        List<UnifiedHeirarchicalObject> result = new ArrayList<>();
    
        if (xPathToData == null || xPathToData.isEmpty()) return result;
    
        // Determine the number of rows based on the size of any one column
        int rowCount = xPathToData.values().iterator().next().size();
    
        for (int i = 0; i < rowCount; i++) {
            UnifiedHeirarchicalObject rowPackage = new UnifiedHeirarchicalObject("package", null);
    
            for (Map.Entry<String, List<UnifiedHeirarchicalObject>> entry : xPathToData.entrySet()) {
                List<UnifiedHeirarchicalObject> columnValues = entry.getValue();
    
                // Defensive check in case of unequal column lengths
                if (i < columnValues.size()) {
                    UnifiedHeirarchicalObject cell = columnValues.get(i);
                    rowPackage.addChild(cell); // Assuming addChild adds a value under the xpath
                }
            }
    
            result.add(rowPackage);
        }
    
        return result;
    }
    
    public String stringifyData() throws Exception {
        String dataString = new DataWriter().writeToString(dataFile, DataFormat.XML);
        return dataString;
    }

    public List<UnifiedHeirarchicalObject> getNodesByXPath(String xpath) {
        return this.xPathToData.get(xpath);
    }

    public Map<String, List<UnifiedHeirarchicalObject>> getXPathToDataMap() {
        return this.xPathToData;
    }

    public UnifiedHeirarchicalObject getDataFile() {
        return this.dataFile;
    }
}
