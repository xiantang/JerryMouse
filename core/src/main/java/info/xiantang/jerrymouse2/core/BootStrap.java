package info.xiantang.jerrymouse2.core;

import info.xiantang.jerrymouse2.core.server.Reactor;
import info.xiantang.jerrymouse2.core.handler.SampleBaseHandler;


import java.io.IOException;

public class BootStrap {

    public static void main(String[] args) throws IOException {
        Reactor reactor = new Reactor(9003, SampleBaseHandler.class);
        reactor.run();
    }
}
