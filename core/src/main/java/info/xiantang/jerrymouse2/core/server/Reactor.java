package info.xiantang.jerrymouse2.core.server;

import info.xiantang.jerrymouse2.core.event.Event;

import java.nio.channels.Selector;

public interface Reactor extends Runnable {
    String getName();

    Selector getSelector();

    void register(Event event);
}
