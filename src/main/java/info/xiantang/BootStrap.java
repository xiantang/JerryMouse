package info.xiantang;

import info.xiantang.core.Reactor;
import info.xiantang.core.SampleBaseHandler;

import java.io.IOException;

public class BootStrap {

    public static void main(String[] args) throws IOException {
        Reactor reactor = new Reactor(9003, SampleBaseHandler.class);
        reactor.run();
    }
}
