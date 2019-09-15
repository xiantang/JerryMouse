package v0_1;

import com.github.apachefoundation.jerrymouse2.AbstractEndpoint;
import com.github.apachefoundation.jerrymouse2.NioEndpoint;
import org.junit.Test;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class TestSocketWrapper {

    @Test
    public void testSocketWrapperInit() throws IOException {
        AbstractEndpoint endpoint = new NioEndpoint();
        SocketChannel socketChannel = SocketChannel.open();
//        NioSocketWrapper socketWrapper = new NioSocketWrapper(socketChannel, endpoint);

    }
}
