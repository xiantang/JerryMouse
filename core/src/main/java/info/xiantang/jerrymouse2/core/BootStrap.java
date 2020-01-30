package info.xiantang.jerrymouse2.core;

import info.xiantang.jerrymouse2.core.handler.SampleBaseHandler;
import info.xiantang.jerrymouse2.core.server.MultiReactor;

import java.io.IOException;

public class BootStrap {

    public static void main(String[] args) throws IOException {
        MultiReactor reactor = new MultiReactor("mainReactorName", 9003, SampleBaseHandler.class, 4);
        reactor.run();
    }
}
