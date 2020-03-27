package info.xiantang.jerrymouse.core.lifecycle;

public interface LifeCycle {
    void init() throws Exception;

    void start();

    void destroy() throws Exception;
}
