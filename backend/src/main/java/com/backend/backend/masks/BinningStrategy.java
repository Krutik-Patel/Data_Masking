package com.backend.backend.masks;

import java.util.List;
import java.util.Map;
import com.backend.backend.utils.UnifiedHeirarchicalObject;

public class BinningStrategy implements MaskingStrategy {
    // Bin size exponent
    private int x;
    private Map<String, Object> parameters;

    // Constructor: Initialize bin size based on parameter x
    public BinningStrategy(Map<String, Object> params) {
        this.parameters = params;
        if (params != null) {
            this.x = Integer.parseInt((String) params.get("x"));
        } else {
            this.x = 1; // Default value if params is null
        }
        // this.x = 1;
    }

    // Mask method: Replace each numerical value with its bin range
    @Override
    public void mask(List<UnifiedHeirarchicalObject> dataSlices) {
        dataSlices.forEach(row -> {
            double n = Double.parseDouble(row.getValue());
            int b = (int) Math.pow(10, x); // Explicit cast to int

            double lower = Math.floor(n / b) * b; // Lower bound of bin
            double upper = lower + b; // Upper bound of bin

            long lowerInt = (long) lower;
            long upperInt = (long) upper;

            String maskedValue = lowerInt + "-" + upperInt;

            row.setValue(maskedValue);
        });
    }

    @Override
    public Map<String, Object> getParameters() { return this.parameters; }
}
