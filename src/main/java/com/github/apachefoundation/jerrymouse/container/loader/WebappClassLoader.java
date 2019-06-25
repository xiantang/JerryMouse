package com.github.apachefoundation.jerrymouse.container.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

/**
 * @Author: xiantang
 * @Date: 2019/6/24 22:09
 */
public class WebappClassLoader extends ClassLoader implements Reloader {

    @Override
    public void addRepository(String repository) {

    }

    public Class<?> findClass(byte[] b) throws ClassNotFoundException {

        return defineClass(null, b, 0, b.length);
    }



    public Class<?> load(String name) throws ClassNotFoundException {
        try {
            return findClass(getBytes(name));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private byte[] getBytes(String filename) throws IOException {
        File file = new File(filename);

        long len = file.length();
        byte raw[] = new byte[(int) len];
        FileInputStream fin = new FileInputStream(file);
        int r = fin.read(raw);
        if (r != len) {
            throw new IOException("Can't read all, " + r + " != " + len);
        }
        fin.close();
        return raw;
    }

    @Override
    public String[] findRepository() {
        return new String[0];
    }

    @Override
    public boolean modified() {
        return false;
    }


}
