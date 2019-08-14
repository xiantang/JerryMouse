package com.github.apachefoundation.jerrymouse.container.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;


/**
 * @Author: xiantang
 * @Date: 2019/6/24 22:09
 */
public class WebappClassLoader extends ClassLoader implements Reloader {
    private boolean modified = false;



    @Override
    public void addRepository(String repository) {

    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (!name.contains("class")) {
            name = "./target/test-classes/" + name.replace(".", "/") + ".class";
        }
        try {
            return findClass0(getBytes(name));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Class<?> findClass0(byte[] b) throws ClassNotFoundException {

        return defineClass(null, b, 0, b.length);
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
        return modified;
    }


    public void setModified(boolean b) {
        this.modified = b;
    }




}
