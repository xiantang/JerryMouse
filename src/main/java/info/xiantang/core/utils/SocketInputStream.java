package info.xiantang.core.utils;

import info.xiantang.core.enumeration.HttpStatus;
import info.xiantang.core.exception.RequestInvalidException;
import info.xiantang.core.http.HttpRequest;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

import static org.apache.log4j.NDC.peek;


/**
 * @Author: xiantang
 * @Date: 2019/4/17 14:45
 * 这个类专门负责读取和解析 http 协议
 * 至于在什么时候解析 还没有定
 */
public class SocketInputStream {
    private SocketChannel socketChannel;
    private final String httpHeadEnd = "\\r\\n\\r\\n";
    private static final int CR = (int) '\r';
    private static final int LF = (int) '\n';
    /**
     * 将数据读到缓冲区
     */
    private ByteBuffer buffer;
    private int pos = 0;
    private int count = 0;



    public SocketInputStream(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
        this.buffer = ByteBuffer.allocate(2048);
    }

    /**
     * 填充requestLineBuffer
     * @param requestBuffer
     * @throws RequestInvalidException
     * @throws IOException
     */
    public void stuffRequestLineBuffer(StringBuilder requestBuffer) throws RequestInvalidException, IOException {
        int ch = -1;
        int head = pos;
        do {
            ch = read();
            if (ch == -1) {
                // 通過在這裏
                //丢弃空包
                throw new RequestInvalidException(HttpStatus.NOT_FOUND);
            }
            requestBuffer.append((char) ch);
        } while (ch != CR && ch != LF);
    }

    /*
     * 读取并解析 http 的第一行
     */
    public void readRequestLine(HttpRequest httpRequest) throws IOException, RequestInvalidException {

    }

    public String decode(String value, String enc) {
        try {
            return URLDecoder.decode(value, enc);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * 读取一个请求头
     */

    private void readHttpBody(HttpRequest httpRequest) throws IOException {
        int contentLength = httpRequest.getContentLength();
        StringBuilder httpBody = new StringBuilder();
        if (contentLength >= 0) {
            for (int i = 0; i < contentLength; i++) {
                int ch = read();
                if (ch == -1){
                    continue; //丢弃空包
                }
                httpBody.append(ch);
            }
            httpRequest.setBody(httpBody.toString());
        } else {
            //变长body 日后支持
        }
    }


    private int read() throws IOException {
        if (pos >= count) {
            fill();

            if (pos >= count) {

                return -1;
            }
        }
        return buffer.get(pos++) & 0xff;
    }

    private int peek() throws IOException {
        if (pos >= count) {
            fill();

            if (pos >= count) {
                return -1;
            }
        }

        return buffer.get(pos) & 0xff;
    }

    public void stuffRequestBuffer(StringBuilder requestLineBuffer) throws IOException, RequestInvalidException {
        while (peek() == CR || peek() == LF || peek() == ' ') {
            read();
        }
        stuffRequestLineBuffer(requestLineBuffer);
    }

    public boolean stuffRequestHeaderBuffer(StringBuilder stringBuilder) throws IOException {
        int i = 0;
        while (peek() == CR || peek() == LF || peek() == ' '){
            read();

            i++;
        }
        if (i >= 2) {
            return false;
        }
        return true;
    }

    private void fill() throws IOException {
        pos = 0;
        count = 0;
        int nRead = socketChannel.read(buffer);
        if (nRead > 0) {
            count = nRead;
        }
    }
}
