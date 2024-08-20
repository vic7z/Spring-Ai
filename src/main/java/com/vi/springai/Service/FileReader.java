package com.vi.springai.Service;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;

public class FileReader {
    File directory = new File("");

    public void loadFile() {
        if (directory.exists() && directory.isDirectory()) {
            FilenameFilter pdfFilter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.toLowerCase().endsWith(".pdf");
                }
            };
            File[] files = directory.listFiles(pdfFilter);
            Arrays.stream(files).map()
        }
    }
}
