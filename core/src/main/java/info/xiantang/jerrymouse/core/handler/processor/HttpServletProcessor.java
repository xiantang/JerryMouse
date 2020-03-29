package info.xiantang.jerrymouse.core.handler.processor;

import info.xiantang.jerrymouse.core.handler.HandlerContext;
import info.xiantang.jerrymouse.core.loader.WebAppLoader;
import info.xiantang.jerrymouse.core.server.ServletWrapper;
import info.xiantang.jerrymouse.core.server.impl.NotFoundServlet;
import info.xiantang.jerrymouse.http.core.HttpRequest;
import info.xiantang.jerrymouse.http.core.HttpResponse;
import info.xiantang.jerrymouse.http.servlet.Servlet;

import java.lang.reflect.Constructor;
import java.util.Map;

import static info.xiantang.jerrymouse.core.utils.CastUtils.cast;

public class HttpServletProcessor implements Processor{
    private HandlerContext context;

    HttpServletProcessor(HandlerContext context) {
        this.context = context;
    }

    @Override
    public void process(HttpRequest request, HttpResponse response) throws Exception {
        Map<String, ServletWrapper> mapper = context.getMapper();
        ServletWrapper wrapper = mapper.get(request.getPath());
        Servlet servlet;
        if (wrapper == null) {
            servlet = new NotFoundServlet();
        }else{
            servlet = wrapper.getServlet();
            if (servlet == null) {
                String className = wrapper.getClassName();
                WebAppLoader loader = context.getLoader();
                Class<? extends Servlet> servletClass = cast(loader.loadClass(className));
                Constructor constructor = servletClass.getDeclaredConstructor();
                constructor.setAccessible(true);
                Object obj = constructor.newInstance();
                servlet = (Servlet)obj;
                wrapper.setServlet(servlet);
                wrapper.setServletClass(servletClass);
            }
        }
        servlet.service(request, response);
    }
}
