package com.github.apachefoundation.jerrymouse.container.context;

import com.github.apachefoundation.jerrymouse.container.Container;
import com.github.apachefoundation.jerrymouse.container.loader.Loader;
import com.github.apachefoundation.jerrymouse.container.loader.WebappClassLoader;
import com.github.apachefoundation.jerrymouse.container.loader.WebappLoader;
import com.github.apachefoundation.jerrymouse.container.mapper.Mapper;
import com.github.apachefoundation.jerrymouse.container.mapper.SimpleContextMapper;
import com.github.apachefoundation.jerrymouse.container.pipeline.Pipeline;
import com.github.apachefoundation.jerrymouse.container.pipeline.SimplePipeline;
import com.github.apachefoundation.jerrymouse.container.pipeline.StandardValveContext;
import com.github.apachefoundation.jerrymouse.container.valve.Valve;
import com.github.apachefoundation.jerrymouse.container.wrapper.Wrapper;
import com.github.apachefoundation.jerrymouse.http.HttpRequest;
import com.github.apachefoundation.jerrymouse.http.HttpResponse;
import org.apache.log4j.Logger;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @Author: xiantang
 * @Date: 2019/5/29 16:24
 */
public class SimpleContext implements Context, Pipeline {
    private List<File> modifiedFiles;
    private Logger logger = Logger.getLogger(SimpleContext.class);
    private Mapper mapper = new SimpleContextMapper(this);
    private Loader loader = null;
    private List<Container> containers = new LinkedList<>();
    private Map<String, String> urlMap = new HashMap<>();
    private Map<String, Wrapper> wrapperMap = new HashMap<>();
    @Override
    public void load() {
        for (Container container : containers) {
            try {
                ((Wrapper) container).load();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void reload() throws NoSuchMethodException, IllegalAccessException, InstantiationException, MalformedURLException, InvocationTargetException, ClassNotFoundException {
        WebappLoader webappLoader = (WebappLoader) getLoader();
        webappLoader.setClassLoader(new WebappClassLoader());
        logger.debug("为WebappLoader 重新设置ClassLoader成员");
        for (File file : modifiedFiles) {
            reload0(file.getPath());
        }
    }

    private void reload0(String path) throws NoSuchMethodException, IllegalAccessException, InstantiationException, ClassNotFoundException, InvocationTargetException, MalformedURLException {
        for(Map.Entry<String, Wrapper> entry : wrapperMap.entrySet()) {
            Wrapper value = entry.getValue();
            String servletClass = value.getServletClass();
            String curClass = path.replace(".\\target\\test-classes\\", "")
                    .replace("\\", ".")
                    .replace(".class", "");
            if (servletClass.equals(curClass)) {
                value.setLoader(getLoader());
                value.load();
                logger.debug("已重新加载 " + servletClass);
            }
        }
    }

    private Pipeline pipeline = new SimplePipeline(this, new StandardValveContext());
    /**
     * 设置默认的mapper
     */



    @Override
    public Valve getBasic() {
        return pipeline.getBasic();
    }

    @Override
    public void setBasic(Valve valve) {
        pipeline.setBasic(valve);
    }


    @Override
    public void invoke(HttpRequest request, HttpResponse response) throws Exception {
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

    public void setModifiedFiles(List<File> res) {
        modifiedFiles = res;
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


    public void backgroundProcess() {
        ((WebappLoader) loader).start();
    }
}
