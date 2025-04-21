package com.backend.backend.utils;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class MultiPartFileUtils {

    // Mapping file extensions to MIME types
    private static final Map<String, String> mimeTypes = new HashMap<>();

    static {
        mimeTypes.put("json", "application/json");
        mimeTypes.put("xml", "application/xml");
        mimeTypes.put("txt", "text/plain");
        mimeTypes.put("html", "text/html");
        // Add other formats if necessary
    }

    public static MultipartFile convertStringToMultipart(String content, String fileName) {
        // Get the file extension from the fileName
        String fileExtension = getFileExtension(fileName).toLowerCase();

        // Default to application/octet-stream if the extension is not recognized
        String mimeType = mimeTypes.getOrDefault(fileExtension, "application/octet-stream");

        // Return the MockMultipartFile with dynamic content type
        return new MockMultipartFile("file", fileName, mimeType, content.getBytes(StandardCharsets.UTF_8));
    }

    // Helper method to extract the file extension from the file name
    private static String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex > 0) {
            return fileName.substring(dotIndex + 1);
        }
        return ""; // Return an empty string if no extension found
    }
}
