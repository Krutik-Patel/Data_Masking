package com.backend.backend.utils;

import org.springframework.web.multipart.MultipartFile;

public interface DataLoaderStrategy {
    public UnifiedHeirarchicalObject parseFile(MultipartFile file) throws Exception;
}
