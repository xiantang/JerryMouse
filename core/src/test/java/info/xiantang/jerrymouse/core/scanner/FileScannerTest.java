package info.xiantang.jerrymouse.core.scanner;

import info.xiantang.jerrymouse.core.scanner.listener.Listener;
import info.xiantang.jerrymouse.core.server.Context;
import info.xiantang.jerrymouse.core.server.HttpServer;
import info.xiantang.jerrymouse.core.utils.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileScannerTest {
    @Test
    public void testFileScan() throws Exception {
        File file = new File("build");
        FileScanner fileScanner = new FileScanner(file,10);
        AtomicInteger counter = new AtomicInteger();
        counter.getAndSet(0);
        Listener listener = event -> counter.getAndIncrement();
        fileScanner.registerListener(listener);
        Thread f = new Thread(fileScanner);
        f.start();
        TimeUnit.MILLISECONDS.sleep(50);
        FileWriter myWriter = new FileWriter("build/test.txt");
        myWriter.write("test");
        myWriter.close();
        TimeUnit.MILLISECONDS.sleep(50);
        FileUtils.deleteDir(new File("build/test.txt"));
        TimeUnit.MILLISECONDS.sleep(50);
        assertEquals(2,counter.get());
    }


    @Test
    public void testFileReplace() throws Exception {
        HttpServer httpServer = new HttpServer();
        httpServer.start();
        TimeUnit.MILLISECONDS.sleep(500);
        File from = new File("build/sample-user.jar");
        File to = new File("tmp/sample-user.jar");
        Files.move(from.toPath(), to.toPath(), StandardCopyOption.REPLACE_EXISTING);
        TimeUnit.MILLISECONDS.sleep(500);
        List<Context> contexts = httpServer.getContexts();
        Files.move(to.toPath(),from.toPath(), StandardCopyOption.REPLACE_EXISTING);
        assertEquals(1, contexts.size());
    }

}
