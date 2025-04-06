package com.backend.backend.engine;

import org.springframework.web.multipart.MultipartFile;

import com.backend.backend.morpher.Morpher;


public class Engine {
    private DataFileLoader dataFileLoader;
    private ConfigLoader configLoader;
    private Morpher morpher;
    private static Engine instance;

    public static Engine getInstance() {
        if (instance == null) {
            instance = new Engine();
        }
        return instance;
    }

    private Engine() {
        this.morpher = new Morpher();
    }
    
    public String maskData(MultipartFile config, MultipartFile data) {
        try {
            this.configLoader = new ConfigLoader();
            this.dataFileLoader = new DataFileLoader();
            this.configLoader.parse(config);
            this.dataFileLoader.parse(data);
            this.morpher.executeOperations(this.configLoader, this.dataFileLoader);
            String outputData = this.dataFileLoader.stringifyData();
            return outputData;
        } catch (Exception e) {
            System.err.println(e);
            return e.toString();
        }
    }
}
