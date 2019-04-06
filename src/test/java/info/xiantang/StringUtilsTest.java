package info.xiantang;

import info.xiantang.core.utils.StringUtils;
import org.junit.Test;
import static org.junit.Assert.*;
public class StringUtilsTest {
    @Test
    public void capitalizeTest() {
        assertEquals("Abc", StringUtils.capitalize("abc"));
    }
}
