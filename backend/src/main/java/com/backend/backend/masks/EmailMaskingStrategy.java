package com.backend.backend.masks;

import java.util.List;
import java.util.Map;

import javax.naming.ldap.UnsolicitedNotificationEvent;

import com.backend.backend.utils.UnifiedHeirarchicalObject;

public class EmailMaskingStrategy implements MaskingStrategy {
    public EmailMaskingStrategy(Map<String, Object> params) {

    }

    public void mask(List<UnifiedHeirarchicalObject> dataSlices) {
        dataSlices.forEach((row) -> {
            row.setValue(this.maskValue(row.getValue()));
        });
    }

    private String maskValue(String value) {
        String[] email_parts = value.split("@");
        return "user" + "@" + email_parts[1];
    }
}
