package info.xiantang.jerrymouse.core.scanner.listener;

import info.xiantang.jerrymouse.core.loader.ContextLoader;
import info.xiantang.jerrymouse.core.scanner.Event;
import info.xiantang.jerrymouse.core.scanner.FileModifyEvent;

import java.io.File;
import java.util.List;

public class FileModifyListener implements Listener {
    private ContextLoader contextLoader;

    public FileModifyListener(ContextLoader contextLoader) {
        this.contextLoader = contextLoader;
    }

    @Override
    public void onEvent(Event event) throws Exception {
        if (event instanceof FileModifyEvent) {
            contextLoader.reload();
        }
    }
}
