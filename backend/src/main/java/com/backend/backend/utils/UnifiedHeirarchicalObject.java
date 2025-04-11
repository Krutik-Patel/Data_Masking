package com.backend.backend.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class UnifiedHeirarchicalObject {
    private final String key;
    private String value;
    private String xPath;
    private List<UnifiedHeirarchicalObject> children;
    private Map<String, UnifiedHeirarchicalObject> nodeByXpath;

    public UnifiedHeirarchicalObject(String key, String value) {
        this.key = key;
        this.value = value;
        this.children = new ArrayList<UnifiedHeirarchicalObject>();
        this.nodeByXpath = new HashMap<>();
    }
    
    public UnifiedHeirarchicalObject(String key, String value, String xPath) {
        this.key = key;
        this.value = value;
        this.xPath = xPath;
        this.children = new ArrayList<UnifiedHeirarchicalObject>();
        this.nodeByXpath = new HashMap<>();
    }

    public String getKey() { return this.key; }
    public String getValue() { return this.value; }
    public String getXpath() { return this.xPath; }
    public void setXpath(String value) { this.xPath = value; }
    public void setValue(String value) { this.value = value; } 
    public boolean hasChildren() { return children.size() > 0; }
    public List<UnifiedHeirarchicalObject> getChildren() { return this.children; }
    public UnifiedHeirarchicalObject getChildByXpath(String xpath) { return this.nodeByXpath.get(xpath); } 
    public UnifiedHeirarchicalObject getNthChild(int index) { return this.children.get(index); }
    public void addChild(UnifiedHeirarchicalObject child) throws Exception { 
        if (child.getXpath() == null) {
            throw new Exception("XPath not initialized...");
        }
        this.nodeByXpath.put(child.getXpath(), child);
        this.children.add(child); 
    }
}


/* assumptions:: 
 
    1. the structure of this tree would not change after data masking
    2. key of a node can't be changed.
    3. The structure of xml has no mixed elements. Either has child nodes or text content.

*/