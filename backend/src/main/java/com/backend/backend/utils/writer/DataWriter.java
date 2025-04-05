package com.backend.backend.utils.writer;

import java.io.FileWriter;

import com.backend.backend.utils.UnifiedHeirarchicalObject;

public class DataWriter {
    private String content;
    private DataWriterStrategy writer;

    public static enum DataFormat {
        XML,
        JSON
    }

    public String writeToString(UnifiedHeirarchicalObject object, DataFormat format) throws Exception {
        switch (format) {
            case XML:
                this.writer = new XMLWriter();
                break;
            case JSON:
                this.writer = new JSONWriter();
                break;
            default:
                throw new IllegalArgumentException("Illegal Argument Passed: " + format);
        }

        this.content = this.writer.writeToString(object);
        return this.content;
    }

    public void dumpFile(String dumpFilePath) throws Exception {
        FileWriter filewriter = new FileWriter(dumpFilePath);
        filewriter.write(this.content);
        filewriter.close();
    }
}
