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
    private boolean reloadable = true;
    private Logger logger = Logger.getLogger(WebappLoader.class);
    private ClassLoader classLoader = null;
    private Set<String> repositories;
    private Map<String, Long> fileMap;

    /**
     * TODO 这个先写死了
     */
    public static final String WEB_ROOT = "file:target/test-classes/";

    private void scanClasses() {

        File file = new File("./target/test-classes/");
        List<File> res = new ArrayList<>();
        List<File> modifiedFiles = new ArrayList<>();
        res = findAllFiles(res, file);
        for (File f:res) {
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



    private List<File> findAllFiles(List res,File file) {
        File[] fs = file.listFiles();
        for (File f : fs) {
            if (f.isDirectory()) {
                res = findAllFiles(res,f);
            } else {
                res.add(f);
            }
        }
        return res;
    }

    @Override
    public void run() {
        while (!threadDone){
            try {
                TimeUnit.SECONDS.sleep(5);
                scanClasses();
                if (!((WebappClassLoader)classLoader).modified()) {
                    continue;
                } else {
                    try {
                        ((SimpleContext) getContainer()).reload();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    ((WebappClassLoader) classLoader).setModified(false);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public synchronized void start() {
        logger.debug("开启新的线程执行类的载入");
        super.start();
    }


    public WebappLoader() {
        createClassLoader();
        repositories = new HashSet<>();
        fileMap = new HashMap<>();
        File file = new File("./target/test-classes/");
        List<File> res = new ArrayList<>();
        res = findAllFiles(res, file);
        for (File f:res) {
            fileMap.put(f.getPath(), f.lastModified());
        }
    }

    private WebappClassLoader createClassLoader()  {
        classLoader = new WebappClassLoader();
        return (WebappClassLoader) classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }


    @Override
    public HttpServlet load(String className) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class clz = ((WebappClassLoader)classLoader).loadClass("./target/test-classes/"+className.replace(".","/")+".class");
        HttpServlet servlet = (HttpServlet) clz.getConstructor().newInstance();
        return servlet;
    }

    @Override
    public ClassLoader getClassLoader() {
        return classLoader;
    }

    @Override
    public Container getContainer() {
        return container;
    }

    @Override
    public void setContainer(Container context) {
        this.container = context;
    }

    @Override
    public void addRepository(String repository) {
        repositories.add(repository);
    }


    @Override
    public boolean modified() {
        return false;
    }

    @Override
    public void setReloadable(boolean reloadable) {
        this.reloadable = reloadable;
    }

    @Override
    public boolean getReloadable() {
        return reloadable;
    }

    @Override
    public void addPropertyChangeListener() {

    }

    @Override
    public String[] findRepositories() {
        return repositories.toArray(new String[repositories.size()]);
    }
}
