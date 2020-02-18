package info.xiantang.jerrymouse.core.loader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ServletClassLoader extends ClassLoader {
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] clsBytes = loadClassFromFile(name);
        return defineClass(name, clsBytes, 0, clsBytes.length);
    }

    private byte[] loadClassFromFile(String name) throws ClassNotFoundException {
        String fileName = name.replace('.', File.separatorChar) + ".class";
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new ClassNotFoundException("can't find file's input stream.");
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int currentValue;

        try {
            while ((currentValue = inputStream.read()) != -1) {
                outputStream.write(currentValue);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return outputStream.toByteArray();
    }


}
