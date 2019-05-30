package com.github.apachefoundation.jerrymouse.container.context;

import com.github.apachefoundation.jerrymouse.container.Contained;
import com.github.apachefoundation.jerrymouse.container.Container;
import com.github.apachefoundation.jerrymouse.container.loader.Loader;
import com.github.apachefoundation.jerrymouse.container.mapper.Mapper;
import com.github.apachefoundation.jerrymouse.container.mapper.SimpleContextMapper;
import com.github.apachefoundation.jerrymouse.container.pipeline.Pipeline;
import com.github.apachefoundation.jerrymouse.container.pipeline.SimplePipeline;
import com.github.apachefoundation.jerrymouse.container.pipeline.StandardValveContext;
import com.github.apachefoundation.jerrymouse.container.valve.Valve;
import com.github.apachefoundation.jerrymouse.container.wrapper.SimpleWrapper;
import com.github.apachefoundation.jerrymouse.container.wrapper.Wrapper;
import com.github.apachefoundation.jerrymouse.context.WebContext;
import com.github.apachefoundation.jerrymouse.context.WebHandler;
import com.github.apachefoundation.jerrymouse.http.HttpRequest;
import com.github.apachefoundation.jerrymouse.http.HttpResponse;
import org.xml.sax.SAXException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.servlet.ServletException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @Author: xiantang
 * @Date: 2019/5/29 16:24
 */
public class SimpleContext implements Context, Pipeline {





    private Pipeline pipeline = new SimplePipeline(this, new StandardValveContext());
    /**
     * 设置默认的mapper
     */
    private Mapper mapper = new SimpleContextMapper(this);
    private List<Mapper> mappers = new LinkedList<>();
    private Loader loader = null;
    private List<Container> containers = new LinkedList<>();
    private Map<String, String> urlMap = new HashMap<>();
    private Map<String, Wrapper> wrapperMap = new HashMap<>();


    @Override
    public Valve getBasic() {
        return pipeline.getBasic();
    }

    @Override
    public void setBasic(Valve valve) {

        pipeline.setBasic(valve);
    }


    @Override
    public void invoke(HttpRequest request, HttpResponse response) throws ServletException, IOException {
        pipeline.invoke(request, response);
    }


    @Override
    public void addServletMapping(String pattern, String name) {
        urlMap.put(pattern, name);
    }

    @Override
    public Mapper findMapper() {
        return mapper;
    }

    /**
     * 该方法用于特殊URL模式的查找
     *
     * @param uri
     * @return
     */
    @Override
    public String findServletMapping(String uri) {
        return urlMap.get(uri);
    }

    @Override
    public Container map(HttpRequest request, boolean b) {

        return mapper.map(request, b);

    }


    @Override
    public void addChild(Container container) {
        containers.add(container);
        String name = ((Wrapper) container).getName();
        ((Wrapper) container).setContainer(container);
        wrapperMap.put(name, (Wrapper) container);
    }

    @Override
    public void removeChild(Container container) {
        container.removeChild(container);
    }

    @Override
    public Container findChild(String name) {
        return wrapperMap.get(name);
    }

    @Override
    public void setLoader(Loader loader) {
        this.loader = loader;
    }

    @Override
    public Loader getLoader() {
        return loader;
    }

    @Override
    public Container[] findChildren() {
        return (Container[]) containers.toArray(new Container[containers.size()]);
    }

    @Override
    public void addValve(Valve valve) {
        throw new NotImplementedException();
    }


    @Override
    public void removeValve(Valve valve) {
        throw new NotImplementedException();
    }
}
