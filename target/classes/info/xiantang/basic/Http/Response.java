package info.xiantang.basic.Http;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;

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


    private Response() {
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
            headInfo = null;
        }

    }

    public Response(OutputStream os) {
        this();
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(os));
    }

    public Response pirnt(String info) {
        content.append(info);
        len += info.getBytes().length;
        return this;
    }

    public Response pirntln(String info) {
        content.append(info).append(CRLF);
        len += (info + CRLF).getBytes().length;
        return this;
    }
    /*
    构建头信息
     */
    private void createHeadInfo(int status) {
        //1
        headInfo.append("HTTP/1.1").append(BLANK);
        headInfo.append(status).append(BLANK);

        switch (status) {
            case 200:
                headInfo.append("OK").append(CRLF);
                break;
            case 404:
                headInfo.append("NOT FOUND").append(CRLF);
            case 505:
                headInfo.append("SEVER ERROR").append(CRLF);
        }
        headInfo.append("Date:").append(new Date()).append(CRLF);
        headInfo.append("Server:").append("X Server/0.0.1;charset=UTF-8").append(CRLF);
        headInfo.append("Content-Type:text/html").append(CRLF);
        headInfo.append("Content-length:").append(len).append(CRLF);
        headInfo.append(CRLF);
    }

    public void pushToBrower(int code) throws IOException {
        if (headInfo == null) {
            code = 505;
        }
        createHeadInfo(code);
        bufferedWriter.append(headInfo);
        bufferedWriter.append(content);
        bufferedWriter.append(CRLF);
        bufferedWriter.flush();

    }
}

