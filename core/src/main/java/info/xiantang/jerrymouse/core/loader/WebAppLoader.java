package info.xiantang.jerrymouse.core.loader;

import info.xiantang.jerrymouse.core.server.ServletWrapper;
import info.xiantang.jerrymouse.http.servlet.Servlet;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static info.xiantang.jerrymouse.core.utils.CastUtils.cast;

public class WebAppLoader {
    private Map<String, ServletWrapper> router;
    private ClassLoader classLoader;

    public WebAppLoader(Map<String, ServletWrapper> router, ClassLoader classLoader) {
        this.router = router;
        this.classLoader = classLoader;
    }

    /**
     * load servlet class and create instance of it.
     * @return return a unmodifiableMap with servlet instances which is load on start up
     * @throws Exception
     */
    public Map<String, ServletWrapper> loadOnStartUp() throws Exception {
        Set<Map.Entry<String, ServletWrapper>> entrySet = router.entrySet();
        for (Map.Entry<String, ServletWrapper> wrapperEntry : entrySet) {
            ServletWrapper wrapper = wrapperEntry.getValue();
            Integer loadOnStartup = wrapper.getLoadOnStartup();
            if (loadOnStartup != null) {
                String className = wrapper.getClassName();
                Class<? extends Servlet> servletClass = cast(classLoader.loadClass(className));
                Servlet servletInstance = servletClass.newInstance();
                wrapper.setServlet(servletInstance);
                wrapper.setServletClass(servletClass);
            }

        }
        return Collections.unmodifiableMap(router);
    }
}
