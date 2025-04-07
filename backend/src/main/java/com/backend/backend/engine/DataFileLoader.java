package com.backend.backend.engine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import org.springframework.web.multipart.MultipartFile;

import com.backend.backend.utils.UnifiedHeirarchicalObject;
import com.backend.backend.utils.loader.DataLoader;
import com.backend.backend.utils.writer.DataWriter;
import com.backend.backend.utils.writer.DataWriter.DataFormat;

public class DataFileLoader {
    private UnifiedHeirarchicalObject dataFile;
    private Map<String, List<UnifiedHeirarchicalObject>> xPathToData;

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
