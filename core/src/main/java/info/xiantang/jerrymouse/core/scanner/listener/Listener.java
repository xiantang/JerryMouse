package info.xiantang.jerrymouse.core.scanner.listener;

import info.xiantang.jerrymouse.core.scanner.Event;

public interface Listener {
    void onEvent(Event event) throws Exception;
}
