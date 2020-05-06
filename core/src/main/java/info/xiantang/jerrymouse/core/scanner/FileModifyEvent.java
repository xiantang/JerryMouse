package info.xiantang.jerrymouse.core.scanner;


import java.io.File;
import java.util.List;

public class FileModifyEvent implements Event {
    private List<File> preModifyFiles;
    private List<File> currentModifyFiles;

    public FileModifyEvent(List<File> preModifyFiles, List<File> currentModifyFiles) {
        this.preModifyFiles = preModifyFiles;
        this.currentModifyFiles = currentModifyFiles;
    }

}
