package info.xiantang.jerrymouse.core.pipeline;

import info.xiantang.jerrymouse.core.handler.ServletContext;
import info.xiantang.jerrymouse.core.server.Context;
import info.xiantang.jerrymouse.core.server.HttpServer;
import info.xiantang.jerrymouse.core.utils.NetUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.List;


public class ContentInBoundPipelineTest {

    @Test
    public void testContextInBoundPipeline() throws Exception {
        int availablePort = NetUtils.getAvailablePort();
        HttpServer httpSever = new HttpServer("/tmp/static");
        List<Context> contexts = httpSever.getContexts();
        Context context = contexts.get(0);
        context.setPort(availablePort);
        context.init();
        context.start();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("http://localhost:" + availablePort + "/index.html");
        CloseableHttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        int length = EntityUtils.toByteArray(entity).length;
        Assertions.assertEquals( 994,length);
    }
}
