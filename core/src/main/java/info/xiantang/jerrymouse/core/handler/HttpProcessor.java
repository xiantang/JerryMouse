package info.xiantang.jerrymouse.core.handler;

import info.xiantang.jerrymouse.core.server.ServletWrapper;
import info.xiantang.jerrymouse.http.core.HttpRequest;
import info.xiantang.jerrymouse.http.core.HttpResponse;
import info.xiantang.jerrymouse.http.servlet.Servlet;

import java.lang.reflect.Constructor;
import java.util.Map;

import static info.xiantang.jerrymouse.core.utils.CastUtils.cast;

class HttpProcessor {
    private HandlerContext context;


    HttpProcessor(HandlerContext context) {
        this.context = context;
    }

    void process(HttpRequest request, HttpResponse response) throws Exception {
        Map<String, ServletWrapper> mapper = context.getMapper();
        ServletWrapper wrapper = mapper.get(request.getPath());
        Servlet servlet = wrapper.getServlet();
        if (servlet == null) {
            String className = wrapper.getClassName();
            ClassLoader loader = context.getLoader();
            Class<? extends Servlet> servletClass = cast(loader.loadClass(className));
            Constructor declaredConstructor = servletClass.getDeclaredConstructor();
            declaredConstructor.setAccessible(true);
            Object obj = declaredConstructor.newInstance();
            servlet = (Servlet)obj;
            wrapper.setServlet(servlet);
            wrapper.setServletClass(servletClass);
        }
        servlet.service(request, response);
    }
}
