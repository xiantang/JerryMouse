package info.xiantang.jerrymouse.core.server;

import info.xiantang.jerrymouse.core.conf.Configuration;
import info.xiantang.jerrymouse.http.utils.EqualsUtils;

public class ServerSource {
    private final String jarName;
    private final Configuration config;

    ServerSource(String jarName, Configuration config) {
        this.jarName = jarName;
        this.config = config;
    }

    String getJarName() {
        return jarName;
    }

    Configuration getConfig() {
        return config;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ServerSource)) {
            return false;
        }

        ServerSource that = (ServerSource) obj;
        try {
            return EqualsUtils.OneDepthContentEquals(this, that);
        } catch (IllegalAccessException e) {
            // TODO use logging
            e.printStackTrace();
        }
        return false;
    }
}
