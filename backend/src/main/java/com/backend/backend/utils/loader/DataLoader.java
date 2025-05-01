package com.backend.backend.utils.loader;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.backend.backend.utils.UnifiedHeirarchicalObject;

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

        System.out.println("The main extension is " + extension);

        UnifiedHeirarchicalObject object = null;
        DataLoaderStrategy loader = this.loaderMap.get(extension);
        object = loader.parseFile(file);

        return object;
    }
}
