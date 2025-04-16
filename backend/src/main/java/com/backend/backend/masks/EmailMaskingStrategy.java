package com.backend.backend.masks;

import java.util.List;
import java.util.Map;

import javax.naming.ldap.UnsolicitedNotificationEvent;

import com.backend.backend.utils.UnifiedHeirarchicalObject;

public class EmailMaskingStrategy implements MaskingStrategy {
    private Map<String, Object> parameters;
    public EmailMaskingStrategy(Map<String, Object> params) {
        this.parameters = params;
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

    @Override
    public Map<String, Object> getParameters() { return this.parameters; }
}
