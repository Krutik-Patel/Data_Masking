package com.backend.backend.engine;

import javax.swing.plaf.multi.MultiPanelUI;

// import org.jcp.xml.dsig.internal.dom.Utils;
import org.springframework.web.multipart.MultipartFile;

import com.backend.backend.morpher.Morpher;
import com.backend.backend.utils.MultiPartFileUtils;

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
            // this.morpher.executeOperations(this.configLoader, this.dataFileLoader);
            this.morpher.executeWaterFallOperations(configLoader, dataFileLoader);
            String outputData = this.dataFileLoader.stringifyData();
            return outputData;
        } catch (Exception e) {
            System.err.println(e);
            return e.toString();
        }
    }

    public String putDataFileFromText(String fileText, String fileName) {
        return putDataFile(MultiPartFileUtils.convertStringToMultipart(fileText, fileName));
    }

    public String putConfigFromText(String fileText, String fileName) {
        return putConfig(MultiPartFileUtils.convertStringToMultipart(fileText, fileName));
    }

    public String putConfig(MultipartFile config) {
        try {
            this.configLoader = new ConfigLoader();
            this.config = config;
            this.configLoader.parse(this.config);
            return this.configLoader.stringifyConfig();
        } catch (Exception e) {
            System.err.println(e);
            return e.toString();
        }
    }

    public String putDataFile(MultipartFile data) {
        try {
            this.dataFileLoader = new DataFileLoader();
            this.data = data;
            this.dataFileLoader.parse(this.data);
            return this.dataFileLoader.stringifyData();
        } catch (Exception e) {
            System.err.println(e);
            return e.toString();
        }
    }

    public boolean isReadyForMasking() {
        return (this.dataFileLoader != null && this.configLoader != null);
    }
}
