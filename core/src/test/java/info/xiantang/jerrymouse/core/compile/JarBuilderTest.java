package info.xiantang.jerrymouse.core.compile;

import info.xiantang.jerrymouse.core.loader.JarClassLoader;
import info.xiantang.jerrymouse.core.utils.FileUtils;
import info.xiantang.jerrymouse.http.servlet.Servlet;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class JarBuilderTest {
    private static final String userPath = System.getProperty("user.dir");
    private static final String rootPath = userPath.replace("core", "sample");

    @Test
    @Ignore
    public void projectParserCanParseProjectToPaths() {
        TargetParser projectParse = new TargetParser(rootPath);
        Project project = projectParse.parse();
        List<File> sources = project.getSources();
        List<File> resources = project.getResources();
        assertTrue(sources.size() > 0);
        assertTrue(resources.size() > 0);
    }

    @Test
    @Ignore
    public void couldCompileMultiJavaFilesToOutFolder() throws Exception {
        TargetParser projectParse = new TargetParser(rootPath);
        Project project = projectParse.parse();
        JarBuilder builder = new JarBuilder(project);
        String jarName = "tmp.jar";
        builder.build(jarName);
        File jar = new File(rootPath + "/out/tmp.jar");
        assertTrue(jar.exists());
        JarClassLoader loader = new JarClassLoader(rootPath + "/out/tmp.jar");
        Class clz = loader.loadClass("info.xiantang.jerrymouse.sample.HelloServlet");
        Constructor declaredConstructor = clz.getDeclaredConstructor();
        declaredConstructor.setAccessible(true);
        Object obj = declaredConstructor.newInstance();
        assertTrue(obj instanceof Servlet);
    }


    @After
    public void clean() {
        File jar = new File(rootPath + "/out/");
        if (jar.exists()) {
            FileUtils.deleteDir(jar);
        }
    }
}
