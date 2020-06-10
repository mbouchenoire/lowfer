package org.lowfer.serde;

import java.io.File;

public class Include {

    private String file;
    private boolean relativeToMasterFile = false;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public boolean isRelativeToMasterFile() {
        return relativeToMasterFile;
    }

    public void setRelativeToMasterFile(boolean relativeToMasterFile) {
        this.relativeToMasterFile = relativeToMasterFile;
    }

    public File toFile(File changelogFile) {
        if (relativeToMasterFile) {
            return new File(changelogFile.getParent(), file);
        } else {
            return new File(file);
        }
    }
}
