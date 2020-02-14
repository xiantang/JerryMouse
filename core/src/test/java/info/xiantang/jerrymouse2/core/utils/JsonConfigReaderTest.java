package info.xiantang.jerrymouse2.core.utils;

import info.xiantang.jerrymouse2.core.conf.Configuration;
import org.apache.commons.codec.Charsets;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class JsonConfigReaderTest {

    @Test
    public void readJsonConfigAsString() throws Exception {
        JsonConfigReader reader = new JsonConfigReader();
        String actual = reader.readAsString("config.json", Charsets.UTF_8);
        String expect = "{\n" +
                "  \"port\": 9000,\n" +
                "  \"subReactorCount\": 3,\n" +
                "  \"router\": {\n" +
                "    \"/\": \"xxx.xxxx.xxxxx.IndexServlet\",\n" +
                "    \"/reg\": \"xxx.xxxx.xxxxx.RegServlet\"\n" +
                "  }\n" +
                "}";
        assertEquals(expect,actual);
    }

    @Test
    public void parseStringAsConfig() throws IOException {
        String raw = "{\n" +
                "  \"port\": 9000,\n" +
                "  \"subReactorCount\": 3,\n" +
                "  \"router\": {\n" +
                "    \"/\": \"xxx.xxxx.xxxxx.IndexServlet\",\n" +
                "    \"/reg\": \"xxx.xxxx.xxxxx.RegServlet\"\n" +
                "  }\n" +
                "}";
        JsonConfigReader reader = new JsonConfigReader();
        Configuration actual = reader.parseStringAsConfiguration(raw);
        Map<String, String> router = new HashMap<>();
        router.put("/", "xxx.xxxx.xxxxx.IndexServlet");
        router.put("/reg", "xxx.xxxx.xxxxx.RegServlet");
        Configuration expect = new Configuration(9000, 3, router);
        assertEquals(expect,actual);
    }


}
