package v0_1;

import com.github.apachefoundation.jerrymouse2.AbstractEndpoint;
import com.github.apachefoundation.jerrymouse2.Acceptor;
import com.github.apachefoundation.jerrymouse2.NioEndpoint;
import org.junit.*;
import org.junit.Test;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * @Author: xiantang
 * @Date: 2019/9/9 20:26
 */
public class TestEndpoint {
    public void sendRequest() throws IOException {
        String hostname = "localhost";
        int port = 8080;
        Socket socket = new Socket(hostname, port);
        OutputStream output = socket.getOutputStream();
        PrintWriter writer = new PrintWriter(output, true);
        writer.println("Test");
        writer.println();
    }


    @Test
    public void test_startInternal() throws Exception {
        AbstractEndpoint endpoint = new NioEndpoint();
        endpoint.startInternal();
        TimeUnit.MILLISECONDS.sleep(200);
        Assert.assertTrue(endpoint.isRunning());
        endpoint.stopInternal();
        Assert.assertFalse(endpoint.isRunning());
    }

    @Test
    public void test_pausedEndpoint() throws Exception {
        AbstractEndpoint endpoint = new NioEndpoint();
        endpoint.bind();
        endpoint.startInternal();
        TimeUnit.MILLISECONDS.sleep(200);
        endpoint.pause();
        Acceptor acceptor = endpoint.getAcceptor();
        TimeUnit.MILLISECONDS.sleep(200);
        sendRequest();
        TimeUnit.MILLISECONDS.sleep(100);
        Assert.assertEquals(Acceptor.AcceptorState.PAUSED, acceptor.getState());
        endpoint.resume();
        TimeUnit.MILLISECONDS.sleep(200);
        Assert.assertEquals(Acceptor.AcceptorState.RUNNING, acceptor.getState());
        endpoint.stopInternal();
    }
}
