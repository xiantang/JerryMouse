package info.xiantang.jerrymouse2.core.loader;

import java.net.URLClassLoader;

import static info.xiantang.jerrymouse2.core.utils.FileUtils.parseSinglePathToUrls;

public class JarClassLoader extends URLClassLoader {

    public JarClassLoader(String jarPath) {
        super(parseSinglePathToUrls(jarPath));
    }


}
