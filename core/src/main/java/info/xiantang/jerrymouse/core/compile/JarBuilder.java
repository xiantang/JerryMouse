package info.xiantang.jerrymouse.core.compile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

public class JarBuilder {
    private static int buffer = 10240;
    private Project project;
    private Path rootPath;
    private String outPutPath;

    JarBuilder(Project project) {
        this.project = project;
        this.rootPath = project.getRootPath();
        this.outPutPath = rootPath.toAbsolutePath() + "/out";
       ;
    }


    public void build(String jarName) throws IOException {
        createTmpDirectory();
        createJarFormClasses(jarName);
    }


    private void createTmpDirectory() throws IOException {
        Files.createDirectory(new File(outPutPath).toPath());
    }


    private void createJarFormClasses(String jarName) throws IOException {
        Map<File, String> result = new HashMap<>();
        List<File> sources = project.getSources();
        for (File file : sources) {
            String path = file.getPath();
            String name = path.substring(path.indexOf("/target/classes") + 16);
            result.put(file, name);
        }
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

}
