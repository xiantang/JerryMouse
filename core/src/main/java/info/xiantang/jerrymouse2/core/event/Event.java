package info.xiantang.jerrymouse2.core.event;

import info.xiantang.jerrymouse2.core.handler.BaseHandler;

import java.io.IOException;

public interface Event {
    void event() throws IOException;

    BaseHandler getHandler();
}
