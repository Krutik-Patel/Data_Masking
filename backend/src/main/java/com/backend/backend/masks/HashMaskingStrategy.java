package com.backend.backend.masks;

import java.util.List;
import java.util.Map;
import org.apache.commons.codec.digest.DigestUtils;

import com.backend.backend.utils.UnifiedHeirarchicalObject;

public class HashMaskingStrategy implements MaskingStrategy {
    private String algorithm;
    public HashMaskingStrategy(Map<String, Object> params) {
        if (params != null) {
            this.algorithm = (String) params.get("algorithm");
            switch (this.algorithm) {
                case "SHA256":
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported Method: " + this.algorithm);
            }
        } else {
            this.algorithm = "SHA256";
        }
    }
    @Override
    public void mask(List<UnifiedHeirarchicalObject> dataSlices) {
        dataSlices.forEach((row) -> {
            row.setValue(this.maskValue(row.getValue()));
        });
    }

    private String maskValue(String value) {
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(value);
        return sha256hex;
    }
}
