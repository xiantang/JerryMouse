package com.github.apachefoundation.jerrymouse.utils;

import com.github.apachefoundation.jerrymouse.http.HttpResponse;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.Writer;
import java.nio.channels.SocketChannel;
import java.util.Date;
/**
 * @Author: xiantang
 * @Date: 2019/4/17 14:45
 */
public class SocketOutputBuffer extends Writer{
    private SocketChannel socketChannel;
    private HttpResponse httpResponse;
    private int bufferUsedCap = 0;
    private Logger logger = Logger.getLogger(SocketOutputBuffer.class);
    public SocketOutputBuffer(SocketChannel socketChannel, HttpResponse httpResponse) {
        this.socketChannel = socketChannel;
        this.httpResponse  = httpResponse;
    }


    /**
     * 將内容寫入bodyBuffer
     * @param cbuf
     * @param off
     * @param len
     */
    @Override
    public void write(char[] cbuf, int off, int len) {
        byte[] btuf = new byte[len - off];
        for (int i = 0, j = off; j < len; i++, j++) {
            btuf[i] = (byte) cbuf[j];
        }

        bufferUsedCap += btuf.length;

        if (httpResponse.getBufferSize() / 0.7 <= bufferUsedCap) {
            httpResponse.setBufferSize(httpResponse.getBufferSize() * 2);
        }
        //TODO:對於過於大的byte流會溢出
        httpResponse.getBodyBuffer().put(btuf);
    }

    @Override
    public void flush() {
        httpResponse.setHeader("Date", String.valueOf(new Date()));
        httpResponse.setHeader("JerryMouse", "X Server/0.0.1;charset=UTF-8");
        httpResponse.setContentLength(bufferUsedCap);
    }


    @Override
    public void close() {
    }
}
