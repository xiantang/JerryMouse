package com.github.apachefoundation.jerrymouse.utils;

import com.github.apachefoundation.jerrymouse.enumeration.HttpStatus;
import com.github.apachefoundation.jerrymouse.exception.RequestInvalidException;
import com.github.apachefoundation.jerrymouse.http.HttpRequest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;


/**
 * @Author: xiantang
 * @Date: 2019/4/17 14:45
 * 这个类专门负责读取和解析 http 协议
 * 至于在什么时候解析 还没有定
 */
public class SocketInputBuffer {
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
    private String remoteAddr;

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public SocketInputBuffer(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
        this.buffer = ByteBuffer.allocate(2048);
        InetAddress addr = socketChannel.socket().getLocalAddress();
        this.remoteAddr = addr.getHostAddress();
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



    public String decode(String value, String enc) {
        try {
            return URLDecoder.decode(value, enc);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
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
