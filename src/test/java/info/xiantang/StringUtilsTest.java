package info.xiantang;

import info.xiantang.basic.utils.StringUtils;
import org.junit.Test;
import static org.junit.Assert.*;
public class StringUtilsTest {
    @Test
    public void capitalizeTest() {
        assertEquals("Abc", StringUtils.capitalize("abc"));
    }
}
