package info.xiantang.jerrymouse.core.compile;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class Project {
    private List<File> sources;
    private List<File> resources;
    private Path rootPath;


    public Project(List<File> sources, List<File> resources, Path rootPath) {
        this.sources = sources;
        this.resources = resources;
        this.rootPath = rootPath;
    }

    public void addSource(File source) {
        sources.add(source);
    }

    public void addResource(File resource) {
        resources.add(resource);
    }

    public List<File> getSources() {
        return sources;
    }


    public List<File> getResources() {
        return resources;
    }

    public Path getRootPath() {
        return rootPath;
    }
}
