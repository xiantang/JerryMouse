package info.xiantang.jerrymouse2.core.server;

import info.xiantang.jerrymouse2.http.servlet.Servlet;
import org.apache.commons.codec.Charsets;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class TestHttpServer {

    @Test
    public void testHttpServerGetCanHandleCertainServlet() throws IOException {
        Map<String, Servlet> mapper = new HashMap<>();
        Servlet servlet = (request, response) -> response.setBody("test\ntest");
        mapper.put("/test",servlet);
        HttpServer server = new HttpServer(8080,mapper);
        server.start();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("http://localhost:8080/test");
        CloseableHttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        String actual = EntityUtils.toString(entity);
        assertEquals("test\ntest", actual);

    }


    @Test
    public void testHttpServerPostCanHandleCertainServlet() throws IOException {
        Map<String, Servlet> mapper = new HashMap<>();
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

        mapper.put("/test",servlet);
        HttpServer server = new HttpServer(8081,mapper);
        server.start();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        StringEntity requestEntity = new StringEntity(
                body,
                ContentType.APPLICATION_JSON);

        HttpPost postMethod = new HttpPost("http://localhost:8081/test");
        postMethod.setEntity(requestEntity);
        HttpResponse rawResponse = httpclient.execute(postMethod);
        HttpEntity entity = rawResponse.getEntity();
        String actual = EntityUtils.toString(entity, Charsets.UTF_8);
        assertEquals(body,actual);

    }

}
