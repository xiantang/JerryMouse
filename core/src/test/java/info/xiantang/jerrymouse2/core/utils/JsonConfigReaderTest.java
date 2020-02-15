package info.xiantang.jerrymouse2.core.utils;

import info.xiantang.jerrymouse2.core.conf.Configuration;
import info.xiantang.jerrymouse2.core.server.ServletWrapper;
import org.apache.commons.codec.Charsets;
import org.junit.Test;

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
                "  \"router\": [\n" +
                "    {\n" +
                "      \"servlet-name\": \"index\",\n" +
                "      \"path\": \"/\",\n" +
                "      \"servlet-class\": \"xxx.xxxx.xxxxx.IndexServlet\",\n" +
                "      \"load-on-startup\": 0\n" +
                "    },\n" +
                "    {\n" +
                "      \"servlet-name\": \"reg\",\n" +
                "      \"path\": \"/reg\",\n" +
                "      \"servlet-class\": \"xxx.xxxx.xxxxx.RegServlet\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        assertEquals(expect,actual);
    }

    @Test
    public void parseStringAsConfig() throws Exception {
        JsonConfigReader reader = new JsonConfigReader();
        String raw = reader.readAsString("config.json", Charsets.UTF_8);
        Configuration actual = reader.parseStringAsConfiguration(raw);
        Map<String, ServletWrapper> router = new HashMap<>();
        ServletWrapper wrapper1 = new ServletWrapper("index", "/", "xxx.xxxx.xxxxx.IndexServlet", 0);
        ServletWrapper wrapper2 = new ServletWrapper("reg", "/reg", "xxx.xxxx.xxxxx.RegServlet", null);
        router.put("/", wrapper1);
        router.put("/reg", wrapper2);
        Configuration expect = new Configuration(9000, 3, router);
        assertEquals(expect,actual);
    }


}
