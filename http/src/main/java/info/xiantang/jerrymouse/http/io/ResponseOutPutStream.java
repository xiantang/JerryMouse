package info.xiantang.jerrymouse.http.io;

import java.io.OutputStream;
import java.nio.ByteBuffer;


public class ResponseOutPutStream extends OutputStream {
    private ByteBuffer buf;

    public ResponseOutPutStream(ByteBuffer buf) {
        this.buf = buf;
    }

    @Override
    public void write(int b) {
        buf.put((byte) b);
    }
}
