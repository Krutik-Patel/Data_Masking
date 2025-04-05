package com.backend.backend.utils.writer;

import com.backend.backend.utils.UnifiedHeirarchicalObject;

public interface DataWriterStrategy {
    public String writeToString(UnifiedHeirarchicalObject object);
}
