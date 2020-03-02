package info.xiantang.jerrymouse.core.compile;

import info.xiantang.jerrymouse.core.utils.FileUtils;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class ProjectCompilerTest {
    private static final String userPath = System.getProperty("user.dir");
    private static final String rootPath = userPath.replace("core", "sample");

    @Test
    public void projectParserCanParseProjectToPaths() {
        ProjectParser projectParse = new ProjectParser(rootPath);
        Project project = projectParse.parse();
        List<File> sources = project.getSources();
        List<File> resources = project.getResources();
        assertTrue(sources.size() > 0);
        assertTrue(resources.size() > 0);
    }

    @Test
    public void couldCompileMultiJavaFilesToOutFolder() throws IOException {
        ProjectParser projectParse = new ProjectParser(rootPath);
        Project project = projectParse.parse();
        ProjectCompiler compiler = new ProjectCompiler(project);
        String jarName = "tmp.jar";
        compiler.compileToJar(jarName);
        File jar = new File(rootPath + "/out/tmp.jar");
        assertTrue(jar.exists());
    }


    @After
    public void clean() {
        File jar = new File(rootPath + "/out/");
        if (jar.exists()) {
            FileUtils.deleteDir(jar);
        }
    }
}
