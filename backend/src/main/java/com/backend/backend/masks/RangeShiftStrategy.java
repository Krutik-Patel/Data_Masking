package com.backend.backend.masks;

import java.util.List;
import java.util.Map;

import com.backend.backend.utils.UnifiedHeirarchicalObject;

public class RangeShiftStrategy implements MaskingStrategy {
    public RangeShiftStrategy(Map<String, Object> params) {

    }

    public void mask(List<UnifiedHeirarchicalObject> dataSlices) {
        dataSlices.forEach((row) -> {
            row.setValue(maskValue(row.getValue()));
        });
    }

    private String maskValue(String value) {
        double num = Double.parseDouble(value);
        double randomPercentage = (double)(Math.random() * 20) - 10;

        double shiftedValue = num + (num * randomPercentage / 100);

        return Double.toString(shiftedValue);
    }
}
