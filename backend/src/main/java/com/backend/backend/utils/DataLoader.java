package com.backend.backend.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public class DataLoader {
    private Map<String, DataLoaderStrategy> loaderMap = new HashMap<>();
    public DataLoader() {
        this.loaderMap.put("xml", new XMLLoader());
        this.loaderMap.put("json", new JSONLoader());
    }

    public UnifiedHeirarchicalObject loadContent(MultipartFile file) throws Exception {
        String originalFilename = file.getOriginalFilename();
        String extension = "";

        if (originalFilename != null && originalFilename.lastIndexOf(".") != -1) {
            extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        }

        UnifiedHeirarchicalObject object = null;
        DataLoaderStrategy loader = this.loaderMap.get(extension);
        object = loader.parseFile(file);

        return object;
    }
}
