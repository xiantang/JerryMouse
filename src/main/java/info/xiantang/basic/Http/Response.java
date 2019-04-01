package info.xiantang.basic.Http;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

/*
1. 内容动态添加
2. 关注状态码，拼接
 */
public class Response {
    private BufferedWriter bufferedWriter;
    // 正文
    private StringBuilder content;
    // 协议头(状态行请求头回车)信息
    private StringBuilder headInfo;
    // 正文的字节数
    private int len = 0;

    private final String  BLANK = " ";
    private final String CRLF = "\r\n";


    public Response() {
        content = new StringBuilder();
        headInfo = new StringBuilder();
        len = 0;
    }

    public Response(Socket client) {
        this();
        try {
            bufferedWriter = new BufferedWriter(
                    new OutputStreamWriter(client.getOutputStream())
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Response(OutputStream os) {
        this();
    }

    private void createHeadInfo(int status) {
        //1
        headInfo.append("HTTP/1.1").append(BLANK);
        headInfo.append(status).append(BLANK);
        headInfo.append("OK").append(CRLF);

    }
}

