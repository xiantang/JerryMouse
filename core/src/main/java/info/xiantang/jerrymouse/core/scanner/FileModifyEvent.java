package info.xiantang.jerrymouse.core.scanner;


import java.io.File;
import java.util.List;

public class FileModifyEvent extends Event {
    private List<File> preModifyFiles;
    private List<File> currentModifyFiles;

    public FileModifyEvent(List<File> preModifyFiles, List<File> currentModifyFiles) {
        this.preModifyFiles = preModifyFiles;
        this.currentModifyFiles = currentModifyFiles;
    }

    public List<File> getCurrentModifyFiles() {
        return currentModifyFiles;
    }

    public List<File> getPreModifyFiles() {
        return preModifyFiles;
    }
}
