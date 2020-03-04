package info.xiantang.jerrymouse.core.compile;

import java.io.File;
import java.util.ArrayList;

public class TargetParser {

    private String rootPath;

    public TargetParser(String rootPath) {
        this.rootPath = rootPath;
    }

    public Project parse() {
        String userPath = rootPath;
        File root = new File(userPath);
        Project project = new Project(
                new ArrayList<>(),
                new ArrayList<>(),
                new File(rootPath).toPath());
        scanRoot(root, project);
        return project;
    }

    private void scanRoot(File root, Project project) {
        if (root.isDirectory()) {
            File[] children = root.listFiles();
            if (children != null) {
                for (File child : children) {
                    scanRoot(child, project);
                }
            }
        } else {
            String path = root.getPath();
            if (path.contains("target/classes") && path.endsWith(".class")) {
                project.addSource(root);
            } else if (path.contains("target/classes")) {
                project.addResource(root);
            }
        }
    }
}
