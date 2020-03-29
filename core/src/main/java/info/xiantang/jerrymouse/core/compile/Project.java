package info.xiantang.jerrymouse.core.compile;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

class Project {
    private List<File> sources;
    private List<File> resources;
    private Path rootPath;


    Project(List<File> sources, List<File> resources, Path rootPath) {
        this.sources = sources;
        this.resources = resources;
        this.rootPath = rootPath;
    }

    void addSource(File source) {
        sources.add(source);
    }

    void addResource(File resource) {
        resources.add(resource);
    }

    List<File> getSources() {
        return sources;
    }


    List<File> getResources() {
        return resources;
    }

    Path getRootPath() {
        return rootPath;
    }
}
