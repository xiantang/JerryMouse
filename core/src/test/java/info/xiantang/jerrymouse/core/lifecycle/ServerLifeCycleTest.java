package info.xiantang.jerrymouse.core.lifecycle;

import info.xiantang.jerrymouse.core.server.FakeServer;
import info.xiantang.jerrymouse.core.utils.NetUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ServerLifeCycleTest {

    @Test(expected = HttpHostConnectException.class)
    public void testServerStartAndStop() throws Exception {
        int availablePort = NetUtils.getAvailablePort();
        int availablePort1 = NetUtils.getAvailablePort();
        List<Integer> ports = new ArrayList<>();
        ports.add(availablePort);
        ports.add(availablePort1);
        LifeCycle server = new FakeServer(ports);
        server.init();
        server.start();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("http://localhost:" + availablePort1 + "/");
        CloseableHttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        String responseStr = EntityUtils.toString(entity);
        assertEquals("/", responseStr);
        server.destroy();
        httpclient.execute(httpget);
    }


}
