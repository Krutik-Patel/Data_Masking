package com.backend.backend;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.backend.backend.engine.Engine;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class ServerController {
    private Engine engineInstance;
    public ServerController() {
        this.engineInstance = Engine.getInstance();
    }

    @GetMapping("/")
    public String index() {
        return "Server has started!";
    }

    @PostMapping("/uploads/data")
    public Response uploadData(@RequestParam("file") MultipartFile data) {

        String successMessage;
        System.err.println(data);
        if (data != null) successMessage = "Data Uploaded Successfully";
        else successMessage = "Data not received!";
        String dataFileText = this.engineInstance.putDataFile(data);
        Response response = new Response(successMessage, dataFileText);
        return response;
    }

    @PostMapping("/uploads/config")
    public Response uploadConfig(@RequestParam("file") MultipartFile data) {
        String configFileText = this.engineInstance.putConfig(data);
        String successMessage = "Config File uploaded Successfully";
        Response response = new Response(successMessage, configFileText);
        return response;
    }

    @GetMapping("/maskData")
    public Response sendMaskedData() {
        String successMessage, maskedOutput;
        if (this.engineInstance.isReadyForMasking()) {
            successMessage = "Masking Completed!";
            maskedOutput = this.engineInstance.maskData();
        } else {
            successMessage = "Masking Failed!";
            maskedOutput = "Upload both data and config file first!!";
        }
        Response returnResponse = new Response(successMessage, maskedOutput);
        return returnResponse; 
    }
}

class Response {
    private String message, additionalText;
    
    public Response(String message) {
        this.message = message;
    }

    public Response(String message, String additionalText) { 
        this.message = message;
        this.additionalText = additionalText;
    }

    public String getMessage() { return this.message; }    
    public String getadditionalText() { return this.additionalText; }
    public void setMessage(String message) { this.message = message; }
    public void setadditionalText(String additionalText) { this.additionalText = additionalText; }
}

