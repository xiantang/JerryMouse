package info.xiantang.jerrymouse.core.utils;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class FileUtilsTest {

    @Test
    public void testReadByteFromFile() throws IOException {
        File image = new File("core/src/test/resources/jerrymouse.jpeg");
        byte[] bytes = FileUtils.readBytes(image);
        long length = image.length();
        assertEquals(length, bytes.length);
    }


    @Test
    public void testReadByteFromJarFile() throws IOException {
        byte[] resource = FileUtils.readFileFromJar(new File("build/sample.jar"), "index.html");
        File html = new File("sample/sample-static-resource/src/main/resources/index.html");
        byte[] bytes = FileUtils.readBytes(html);
        assertArrayEquals(bytes,resource);
    }
}
