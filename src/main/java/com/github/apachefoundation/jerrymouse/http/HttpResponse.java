package com.github.apachefoundation.jerrymouse.http;

import com.github.apachefoundation.jerrymouse.constants.Constants;
import com.github.apachefoundation.jerrymouse.utils.SocketOutputBuffer;


import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
/**
 * @Author: xiantang
 * @Date: 2019/4/17 14:45
 */
public class HttpResponse {
    private static final int BUFFER_SIZE = 1024;
    private SocketChannel socketChannel;
    //cookies

    //http header 字段
    private Map<String, String> headersMap;
    //状态字 默认200了
    private int status = 200;
    //状态信息
    private String statusInfo;
    //错误信息
    private String errorInfo;
    //字符集
    private String characterEncoding;
    //报文长度
    private int contentLength;
    private long contentLengthLong;
    //报文长度
    private String contentType;
    //报文正文
    private ByteBuffer bodyBuffer;
    //是否可提交
    private boolean isCommitted;



    public HttpRequest getRequest() {
        return request;
    }

    public void setRequest(HttpRequest request) {
        this.request = request;
    }

    private HttpRequest request;

    private static final String  BLANK = " ";
    private static final String CRLF = "\r\n";

    public HttpResponse(SocketChannel socketChannel) throws IOException {
        this.socketChannel = socketChannel;
        this.headersMap = new HashMap<>();
        this.bodyBuffer = ByteBuffer.allocate(204800);
    }


    /**
     * 這裏需要修改
     * @return
     */
    @Override
    public String toString() {
        // TODO:我覺得這裏應該是把整個response
        // 給轉換為String 而不是單純的一個header
        StringBuilder resp = new StringBuilder();
        resp.append("HTTP/1.1").append(BLANK);
        resp.append(status).append(BLANK);

        switch (status) {
            case 200:
                resp.append("OK").append(CRLF);
                break;
            case 404:
                resp.append("NOT FOUND").append(CRLF);
                break;
            case 505:
                resp.append("SEVER ERROR").append(CRLF);
                break;
            default:
                resp.append(errorInfo);
        }

        Set<String> keySet = headersMap.keySet();

        for (String key: keySet) {
            resp.append(key);
            resp.append(":");
            resp.append(headersMap.get(key));
            resp.append(CRLF);
        }
        resp.append(CRLF);
        return resp.toString();
    }

    public PrintWriter getWriter()  {
        return new PrintWriter(new SocketOutputBuffer(socketChannel, this));
    }


    // TODO: 这里的代码可以将静态文件读入cache
    public void sendStaticResource() throws IOException {
        FileInputStream fis;
        try {
            File file = new File(Constants.WEB_ROOT, request.getRequestURI());
            fis = new FileInputStream(file);
            FileChannel fileChannel = fis.getChannel();
            if (file.exists()) {
                while (true) {
                    int eof = fileChannel.read(getBodyBuffer());
                    if(eof == -1 ){ break;}
                }
            }

        } catch (FileNotFoundException e) {
            String errorMessage = "<html>" +
                    "<p> HTTP/1.1 404 File Not Found</p>\r\n" +
                    "<p> Content-Type: text/html</p>\r\n" +
                    "\r\n" +
                    "<h1>File Not Found</h1></html>";
            getBodyBuffer().put(errorMessage.getBytes());

        }
    }


    public void setContentLength(int i) {

        headersMap.put("content-length", i + "");
        this.contentLength = i;
    }



    public void setBufferSize(int i) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(i);
        byteBuffer.put(bodyBuffer);
        bodyBuffer = byteBuffer;
    }


    public int getBufferSize() {
        return bodyBuffer.capacity();
    }

    /**
     * capacity 容納的最大數量
     * limit 指的是缓冲区中第一个不能读写的元素的数组下标索引，也可以认为是缓冲区中实际元素的数量
     * position 指的是下一个要被读写的元素的数组下标索引，该值会随get()和put()的调用自动更新
     * @throws IOException
     */
    public void flushBuffer() throws IOException {

        // 獲取put之後的position
        // 作爲之後clear 之後的limit
        int limit = bodyBuffer.position();
        // 它并没有清除数据，只是把position设置第一个位置上
        // 但是他會把limit 設置為buffer的capacity
        bodyBuffer.clear();
        int bodyLength = limit - bodyBuffer.position();
        setContentLength(bodyLength);
        socketChannel.write(ByteBuffer.wrap(this.toString().getBytes()));
        socketChannel.write(bodyBuffer);
    }



    public void setHeader(String s, String s1) {
        headersMap.put(s, s1);
    }

    public int getStatus() {
        return status;
    }


    public ByteBuffer getBodyBuffer() {
        return bodyBuffer;
    }

}
