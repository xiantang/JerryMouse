package info.xiantang.jerrymouse2.core.server;


import info.xiantang.jerrymouse2.http.utils.EqualsUtils;

public class ServletWrapper {
    private final String name;
    private final String path;
    private final String className;
    private Integer loadOnStartup;

    public ServletWrapper(String name, String path, String className,Integer loadOnStartup) {
        this.name = name;
        this.path = path;
        this.className = className;
        this.loadOnStartup = loadOnStartup;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ServletWrapper)) {
            return false;
        }

        ServletWrapper that = (ServletWrapper) obj;
        try {
            return EqualsUtils.OneDepthContentEquals(this, that);
        } catch (IllegalAccessException e) {
            // TODO use logging
            e.printStackTrace();
        }
        return false;
    }
}
