package info.xiantang.jerrymouse.core.utils;

import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class StaticResourcesUtilsTest {

    @Test
    public void testVerificationStaticResourcePath() {
        String path2 = "/favicon.ico";
        boolean result = StaticResourcesUtils.verifyPath(path2);
        assertTrue(result);
        String path1 = "/";
        boolean result1 = StaticResourcesUtils.verifyPath(path1);
        assertFalse(result1);
        String path3 = "/favicon.png";
        boolean result3 = StaticResourcesUtils.verifyPath(path3);
        assertTrue(result3);
        String path4 = "/favicon.jpg";
        boolean result4 = StaticResourcesUtils.verifyPath(path4);
        assertTrue(result4);
        String path5 = "/test";
        boolean result5 = StaticResourcesUtils.verifyPath(path5);
        assertFalse(result5);
        String path6 = "/aaa.html";
        boolean result6 = StaticResourcesUtils.verifyPath(path6);
        assertTrue(result6);
    }

}
