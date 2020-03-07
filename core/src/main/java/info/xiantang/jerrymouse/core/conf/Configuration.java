package info.xiantang.jerrymouse.core.conf;

import info.xiantang.jerrymouse.core.server.ServletWrapper;
import info.xiantang.jerrymouse.http.utils.EqualsUtils;

import java.util.Map;

public class Configuration {
    private final int port;
    private final int subReactorNum;
    private final Map<String, ServletWrapper> router;

    public Configuration(int port, int subReactorNum, Map<String, ServletWrapper> router) {
        this.port = port;
        this.subReactorNum = subReactorNum;
        this.router = router;
    }

    public int getSubReactorNum() {
        return subReactorNum;
    }

    public Map<String, ServletWrapper> getRouter() {
        return router;
    }

    public int getPort() {
        return port;
    }



    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Configuration)) {
            return false;
        }

        Configuration that = (Configuration) obj;
        try {
            return EqualsUtils.OneDepthContentEquals(this, that);
        } catch (IllegalAccessException e) {
            // TODO use logging
            e.printStackTrace();
        }
        return false;
    }
}
