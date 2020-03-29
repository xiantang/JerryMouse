package info.xiantang.jerrymouse.core.handler.processor;

import info.xiantang.jerrymouse.http.core.HttpRequest;
import info.xiantang.jerrymouse.http.core.HttpResponse;

public interface Processor {
    void process(HttpRequest request, HttpResponse response) throws Exception;
}
