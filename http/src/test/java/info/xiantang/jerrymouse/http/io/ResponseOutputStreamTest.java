package info.xiantang.jerrymouse.http.io;

import com.google.common.primitives.Bytes;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ResponseOutputStreamTest {


    @Test
    public void testRequestOutputStreamReadContent() throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(2048);
        OutputStream outputStream = new ResponseOutPutStream(buf);
        outputStream.write("test".getBytes());
        outputStream.write("test".getBytes());
        outputStream.write("test".getBytes());
        buf.flip();
        List<Byte> bytes = new ArrayList<>();
        while (buf.hasRemaining()) {
            bytes.add(buf.get());
        }
        assertEquals("testtesttest", new String(Bytes.toArray(bytes)));
    }


}
