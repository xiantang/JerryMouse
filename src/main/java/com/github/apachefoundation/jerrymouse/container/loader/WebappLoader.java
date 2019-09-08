package com.github.apachefoundation.jerrymouse.container.loader;

import com.github.apachefoundation.jerrymouse.servlet.HttpServlet;
import com.github.apachefoundation.jerrymouse.container.Container;
import com.github.apachefoundation.jerrymouse.container.context.SimpleContext;
import org.apache.log4j.Logger;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author: xiantang
 * @Date: 2019/6/24 21:56
 */
public class WebappLoader extends Thread implements Loader {

    private Container container;
    private boolean threadDone = false;
    private Logger logger = Logger.getLogger(WebappLoader.class);
    private ClassLoader classLoader = null;
    private Set<String> repositories;
    private Map<String, Long> fileMap;


    private void scanClasses() {

        File file = new File("./target/test-classes/");
        List<File> res = new ArrayList<>();
        List<File> modifiedFiles = new ArrayList<>();
        res = findAllFiles(res, file);
        for (File f : res) {
            if (f.lastModified() > fileMap.get(f.getPath())) {
                System.out.println(f.getPath());
                fileMap.put(f.getPath(), f.lastModified());
                modifiedFiles.add(f);
            }
        }
        if (modifiedFiles.size() > 0) {
            ((WebappClassLoader) classLoader).setModified(true);
            ((SimpleContext) getContainer()).setModifiedFiles(modifiedFiles);
        }

    }


    private List<File> findAllFiles(List res, File file) {
        File[] fs = file.listFiles();
        assert fs != null;
        for (File f : fs) {
            if (f.isDirectory()) {
                res = findAllFiles(res, f);
            } else {
                res.add(f);
            }
        }
        return res;
    }

    @Override
    public void run() {
        while (!threadDone) {
            try {
                TimeUnit.SECONDS.sleep(5);
                scanClasses();
                if (((WebappClassLoader) classLoader).modified()) {
                    try {
                        ((SimpleContext) getContainer()).reload();
                    } catch (Exception e) {
                        logger.error(e.getStackTrace());
                    }
                    ((WebappClassLoader) classLoader).setModified(false);
                }
            } catch (InterruptedException e) {
                logger.error(e.getStackTrace());
            }
        }
    }


    public WebappLoader() {
        createClassLoader();
        repositories = new HashSet<>();
        fileMap = new HashMap<>();
        File file = new File("./target/test-classes/");
        List<File> res = new ArrayList<>();
        res = findAllFiles(res, file);
        for (File f : res) {
            fileMap.put(f.getPath(), f.lastModified());
        }
    }

    private WebappClassLoader createClassLoader() {
        classLoader = new WebappClassLoader();
        return (WebappClassLoader) classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }


    @Override
    public HttpServlet load(String className) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class clz = ((WebappClassLoader) classLoader).loadClass("./target/test-classes/" + className.replace(".", "/") + ".class");
        HttpServlet servlet = (HttpServlet) clz.getConstructor().newInstance();
        return servlet;
    }



    @Override
    public Container getContainer() {
        return container;
    }

    @Override
    public void setContainer(Container context) {
        this.container = context;
    }


}
