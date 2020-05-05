package info.xiantang.jerrymouse.core.scanner;

import info.xiantang.jerrymouse.core.utils.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileScannerTest {
    @Test
    public void testFileScan() throws Exception {

        File file = new File("build");
        FileScanner fileScanner = new FileScanner(file);
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


}
