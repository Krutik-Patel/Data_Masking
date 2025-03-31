package com.backend.backend;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class ServerController {
    @GetMapping("/")
    public String index() {
        return "Server has started!";
    }

    @PostMapping("/uploads/data")
    public DataResponse uploadData(String data) {
        return new DataResponse("Data uploaded");
    }

    @PostMapping("/uploads/config")
    public ConfigResponse uploadConfig(String data) {
        return new ConfigResponse("Config Uploaded", "Rules are rules");
    }

    @GetMapping("/maskData")
    public String sendMaskedData() {
        return "Masked Data Sent";
    }
}

class ConfigResponse {
    private String message, configRules;
    
    public ConfigResponse(String message, String configRules) { 
        this.message = message;
        this.configRules = configRules;
    }

    public String getMessage() { return this.message; }
    
    public String getConfigRules() { return this.configRules; }

    public void setMessage(String message) { this.message = message; }

    public void setConfigRules(String configRules) { this.configRules = configRules; }
}

class DataResponse {
    private String message;

    public DataResponse(String message) {
        this.message = message;
    }

    public String getMessage() { return this.message; }

    public void setMessage(String message) { this.message = message; }
}