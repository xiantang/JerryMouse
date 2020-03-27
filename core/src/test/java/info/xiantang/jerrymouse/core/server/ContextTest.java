package info.xiantang.jerrymouse.core.server;

import info.xiantang.jerrymouse.core.conf.Configuration;
import info.xiantang.jerrymouse.core.lifecycle.LifeCycle;
import info.xiantang.jerrymouse.core.utils.NetUtils;
import info.xiantang.jerrymouse.http.servlet.Servlet;
import org.apache.commons.codec.Charsets;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ContextTest {

    @Test
    public void testHttpServerGetCanHandleCertainServlet() throws Exception {
        int availablePort = NetUtils.getAvailablePort();
        Map<String, ServletWrapper> mapper = new HashMap<>();
        Servlet servlet = (request, response) -> response.setBody("test\ntest");
        mapper.put("/test",new ServletWrapper("","/test","aaa",null,servlet.getClass(),servlet));
        Configuration configuration = new Configuration(availablePort, 3, mapper);
        Context context = new Context("jarName", configuration);
        context.start();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("http://localhost:" + availablePort + "/test");
        CloseableHttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        String actual = EntityUtils.toString(entity);
        assertEquals("test\ntest", actual);
    }


    @Test(expected = HttpHostConnectException.class)
    public void testContextStartAndStop() throws Exception {
        int availablePort = NetUtils.getAvailablePort();
        Map<String, ServletWrapper> mapper = new HashMap<>();
        Servlet servlet = (request, response) -> response.setBody("test\ntest");
        mapper.put("/test", new ServletWrapper("", "/test", "aaa", null, servlet.getClass(), servlet));
        Configuration configuration = new Configuration(availablePort, 3, mapper);
        LifeCycle context = new Context("jarName", configuration);
        context.init();
        context.start();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("http://localhost:" + availablePort + "/test");
        CloseableHttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        String actual = EntityUtils.toString(entity);
        assertEquals("test\ntest", actual);
        context.destroy();
        httpclient.execute(httpget);

    }


    @Test
    public void testHttpServerPostCanHandleCertainServlet() throws Exception {
        int availablePort = NetUtils.getAvailablePort();
        Map<String, ServletWrapper> mapper = new HashMap<>();
        String body = "{\n" +
                "    \"Name\":\"李念\",\n" +
                "    \"Stature\":\"163cm\",\n" +
                "    \"Birthday\":\"1985年5月30日\",\n" +
                "    \"Birthplace\":\"湖北省荆门市京山县\",\n" +
                "    \"Info\":\"喜欢电视剧版《蜗居》中李念扮演的郭海藻，为六六的小说《蜗居》中的女主角之一，郭海萍的妹妹。\",\n" +
                "    \"Still\":\"https://cdn.www.sojson.com/study/linian.jpg\"\n" +
                "}";
        Servlet servlet = (request, response) -> {
            String body1 = request.getBody();
            response.setBody(body1);
        };

        mapper.put("/test",new ServletWrapper("","/test","aaa",null,servlet.getClass(),servlet));
        Configuration configuration = new Configuration(availablePort, 3, mapper);
        Context server = new Context("jarName", configuration);
        server.start();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        StringEntity requestEntity = new StringEntity(
                body,
                ContentType.APPLICATION_JSON);

        HttpPost postMethod = new HttpPost("http://localhost:" + availablePort + "/test");
        postMethod.setEntity(requestEntity);
        HttpResponse rawResponse = httpclient.execute(postMethod);
        HttpEntity entity = rawResponse.getEntity();
        String actual = EntityUtils.toString(entity, Charsets.UTF_8);
        assertEquals(body,actual);

    }

}
