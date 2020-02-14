package info.xiantang.jerrymouse2.core.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.xiantang.jerrymouse2.core.conf.Configuration;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static info.xiantang.jerrymouse2.core.utils.CastUtils.cast;

public class JsonConfigReader {
    public String readAsString(String path, Charset encoding) throws Exception {
        URI uri = ClassLoader.getSystemResource(path).toURI();
        byte[] encoded = Files.readAllBytes(Paths.get(uri));
        return new String(encoded, encoding);
    }

    public Configuration parseStringAsConfiguration(String rawStr) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,Object>  rawConfigMap = objectMapper.readValue(rawStr, new TypeReference<HashMap>(){});
        return mapToConfiguration(rawConfigMap);
    }

    private Configuration mapToConfiguration(Map<String, Object> rawConfigMap) {
        Integer port = (Integer)rawConfigMap.get("port");
        Integer subReactorCount = (Integer)rawConfigMap.get("subReactorCount");
        Map<String, String> router = cast(rawConfigMap.get("router"));
        return new Configuration(port, subReactorCount, router);
    }


}
