package com.backend.backend;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.backend.backend.engine.Engine;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class ServerController {
    private MultipartFile dataFile;
    private MultipartFile configFile;
    private Engine engineInstance;
    public ServerController() {
        this.engineInstance = Engine.getInstance();
    }

    @GetMapping("/")
    public String index() {
        return "Server has started!";
    }

    @PostMapping("/uploads/data")
    public Response uploadData(MultipartFile data) {
        this.dataFile = data;
        String output = "Data Uploaded Successfully";
        Response response = new Response(output);
        return response;
    }

    @PostMapping("/uploads/config")
    public Response uploadConfig(MultipartFile data) {
        this.configFile = data;
        String output = "Config File uploaded Successfully";
        Response response = new Response(output);
        return response;
    }

    @GetMapping("/maskData")
    public Response sendMaskedData() {
        String output;
        if (this.dataFile != null && this.configFile != null) {
            output = this.engineInstance.maskData(configFile, dataFile);
        } else {
            output = "Upload both data and config file first!!";
        }
        Response returnResponse = new Response(output);
        return returnResponse; 
    }
}

class Response {
    private String message, configRules;
    
    public Response(String message) {
        this.message = message;
    }

    public Response(String message, String configRules) { 
        this.message = message;
        this.configRules = configRules;
    }

    public String getMessage() { return this.message; }
    
    public String getConfigRules() { return this.configRules; }

    public void setMessage(String message) { this.message = message; }

    public void setConfigRules(String configRules) { this.configRules = configRules; }
}

