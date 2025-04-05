package com.backend.backend.utils.loader;

import org.springframework.web.multipart.MultipartFile;

import com.backend.backend.utils.UnifiedHeirarchicalObject;

public interface DataLoaderStrategy {
    public UnifiedHeirarchicalObject parseFile(MultipartFile file) throws Exception;
}
