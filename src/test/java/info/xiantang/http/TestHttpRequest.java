package info.xiantang.http;

import com.github.apachefoundation.jerrymouse.http.HttpRequest;
import org.junit.Assert;
import org.junit.Test;

/**
 * @Author: xiantang
 * @Date: 2019/8/14 22:38
 */
public class TestHttpRequest {
    @Test
    public void test_HeadersMap() {
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setHeader("Connection", "keep-alive");
        String connection = httpRequest.getHeader("Connection");
        Assert.assertEquals(connection,"keep-alive");
    }

    @Test
    public void test_ParametersMap() {
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setParameter("id", "12");
        String id = httpRequest.getParameter("id");
        Assert.assertEquals("12", id);
    }

}
