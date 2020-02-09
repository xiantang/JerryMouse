package info.xiantang.jerrymouse2.http;

import info.xiantang.jerrymouse2.core.reactor.MultiReactor;
import info.xiantang.jerrymouse2.http.handler.HttpHandler;

import java.io.IOException;

public class BootStrap {

    public static void main(String[] args) throws IOException {
        MultiReactor reactor = new MultiReactor("mainReactorName", 9003, HttpHandler.class, 4);
        reactor.run();
    }
}