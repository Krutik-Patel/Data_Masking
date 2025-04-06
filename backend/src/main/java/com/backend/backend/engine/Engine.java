package com.backend.backend.engine;

import javax.swing.plaf.multi.MultiPanelUI;

import org.springframework.web.multipart.MultipartFile;

import com.backend.backend.morpher.Morpher;


public class Engine {
    private DataFileLoader dataFileLoader;
    private ConfigLoader configLoader;
    private Morpher morpher;
    private static Engine instance;
    private MultipartFile data, config;

    public static Engine getInstance() {
        if (instance == null) {
            instance = new Engine();
        }
        return instance;
    }

    private Engine() {
        this.morpher = new Morpher();
    }
    
    public String maskData() {
        try {
            this.morpher.executeOperations(this.configLoader, this.dataFileLoader);
            String outputData = this.dataFileLoader.stringifyData();
            return outputData;
        } catch (Exception e) {
            System.err.println(e);
            return e.toString();
        }
    }
    
    public String putConfig(MultipartFile config) {
        try {
            this.configLoader = new ConfigLoader();
            this.configLoader.parse(this.config);
            return this.configLoader.stringifyConfig();
        } catch(Exception e) {
            System.err.println(e);
            return e.toString();
        }
    }
    
    public String putDataFile(MultipartFile data) {
        try {
            this.dataFileLoader = new DataFileLoader();
            this.dataFileLoader.parse(this.data);
            return this.dataFileLoader.stringifyData();
        } catch (Exception e) {
            System.err.println(e);
            return e.toString();
        }
    }

    public boolean isReadyForMasking() { return (this.dataFileLoader != null && this.configLoader != null); }
}
