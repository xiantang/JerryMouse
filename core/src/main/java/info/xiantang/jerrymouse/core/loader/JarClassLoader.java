package info.xiantang.jerrymouse.core.loader;

import java.net.URLClassLoader;

import static info.xiantang.jerrymouse.core.utils.FileUtils.parseSinglePathToUrls;

public class JarClassLoader extends URLClassLoader {

    public JarClassLoader(String jarPath) {
        super(parseSinglePathToUrls(jarPath));
    }


}
