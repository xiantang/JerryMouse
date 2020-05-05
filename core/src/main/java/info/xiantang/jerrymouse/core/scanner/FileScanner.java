package info.xiantang.jerrymouse.core.scanner;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FileScanner implements Runnable{
    private File buildRoot;
    private int interval;
    private List<Listener> listeners = new LinkedList<>();
    private List<File> preFiles = new ArrayList<>();


    public FileScanner(File buildRoot,int interval) {
        this.buildRoot = buildRoot;
        this.interval = interval;
        init();
    }

    public FileScanner(File buildRoot) {
        this(buildRoot, 500);
    }

    private void init() {
        preFiles = findNewestFiles();
    }

    public void registerListener(Listener listener) {
        listeners.add(listener);
    }

    private List<File> findNewestFiles() {
        List<File> files = new LinkedList<>();
        getAllFileInDir(buildRoot,files);
        return files;
    }

    private void getAllFileInDir(File root, List<File> currentFiles) {
        if (root.isDirectory()) {
            File[] files = root.listFiles();
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                getAllFileInDir(file, currentFiles);
            }
        } else {
            currentFiles.add(root);
        }

    }


    @Override
    public void run() {
        while (true) {
            try {
                TimeUnit.MILLISECONDS.sleep(interval);
                List<File> newestFiles = findNewestFiles();
                if (!newestFiles.equals(preFiles)) {
                    for (Listener listener : listeners) {
                        FileModifyEvent fileModifyEvent = new FileModifyEvent(preFiles, newestFiles);
                        listener.onEvent(fileModifyEvent);
                        preFiles = newestFiles;
                    }
                }


            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
