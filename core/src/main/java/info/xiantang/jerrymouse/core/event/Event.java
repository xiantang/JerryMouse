package info.xiantang.jerrymouse.core.event;

import info.xiantang.jerrymouse.core.handler.BaseHandler;

import java.io.IOException;

public interface Event {
    void event() throws IOException;
    BaseHandler getHandler();
}
