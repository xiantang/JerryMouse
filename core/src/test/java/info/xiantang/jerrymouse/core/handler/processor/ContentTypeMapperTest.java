package info.xiantang.jerrymouse.core.handler.processor;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @Author: xiantang
 * @Date: 2020/4/25 20:26
 */
public class ContentTypeMapperTest {

    @Test
    public void testCssType() {
        ContentTypeMapper mapper = new ContentTypeMapper();
        String type = mapper.get("css");
        assertEquals("text/css", type);
    }



}
