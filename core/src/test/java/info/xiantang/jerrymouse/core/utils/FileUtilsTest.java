package info.xiantang.jerrymouse.core.utils;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class FileUtilsTest {

    @Test
    public void testReadByteFromFile() throws IOException {
        File image = new File("src/test/resources/jerrymouse.jpeg");
        byte[] bytes = FileUtils.readBytes(image);
        long length = image.length();
        assertEquals(length, bytes.length);
    }
}
