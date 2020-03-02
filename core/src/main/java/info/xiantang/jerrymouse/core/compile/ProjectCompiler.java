package info.xiantang.jerrymouse.core.compile;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

public class ProjectCompiler {
    private static int buffer = 10240;
    private JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    private Project project;
    private Path rootPath;
    private String outPutPath;
    private String outClassesPath;

    public ProjectCompiler(Project project) {
        this.project = project;
        this.rootPath = project.getRootPath();
        this.outPutPath = rootPath.toAbsolutePath() + "/out";
        this.outClassesPath = rootPath.toAbsolutePath() + "/out/classes";
    }


    public void compileToJar(String jarName) throws IOException {
        createTmpDirectory();
        genClassesFormProject();
        createJarFormClasses(jarName);
    }


    private void createTmpDirectory() throws IOException {
        Files.createDirectory(new File(outPutPath).toPath());
        Files.createDirectory(new File(outClassesPath).toPath());
    }

    private void genClassesFormProject() {
        List<File> sources = project.getSources();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        List<String> options = buildOptions();
        Iterable<? extends JavaFileObject> preCompileFiles = fileManager.getJavaFileObjectsFromFiles(sources);
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null, options, null, preCompileFiles);
        task.call();
    }

    private void findAllClassesInPath(File root, Map<File, String> all) {
        if (root.isDirectory()) {
            File[] children = root.listFiles();
            if (children != null) {
                for (File child : children) {
                    findAllClassesInPath(child, all);
                }
            }
        } else {
            if (root.getName().endsWith(".class")) {
                String path = root.getPath();
                String name = path.substring(path.indexOf("/out/classes") + 13);
                all.put(root, name);
            }
        }

    }

    private void createJarFormClasses(String jarName) throws IOException {
        Map<File, String> result = new HashMap<>();
        findAllClassesInPath(new File(outClassesPath), result);
        Iterator<File> files = result.keySet().iterator();
        FileOutputStream fos = new FileOutputStream(outPutPath + "/" + jarName);
        Manifest manifest = new Manifest();
        manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
        JarOutputStream jos = new JarOutputStream(fos, manifest);
        BufferedOutputStream bos = new BufferedOutputStream(jos);
        while (files.hasNext()) {
            File file = files.next();
            jos.putNextEntry(new JarEntry(result.get(file)));
            FileInputStream fin = new FileInputStream(file);
            byte bytes[] = new byte[buffer];
            while (true) {
                int len = fin.read(bytes, 0, bytes.length);
                if (len <= 0)
                    break;
                jos.write(bytes, 0, len);
            }
            fin.close();
        }
        jos.close();
        fos.close();
        bos.close();
    }

    private List<String> buildOptions() {
        List<String> options = new ArrayList<>();

        Path rootPath = project.getRootPath();
        String outSrcPath = rootPath.toAbsolutePath() + "/out/classes";

        options.add("-d");
        options.add(outSrcPath);

        return options;
    }
}
