package info.xiantang.basic.Http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.*;

/*
封装请求协议
封装请求参数为map
 */
public class Request {
    // 协议信息
    private String requestInfo;
    // 请求方式
    private String method;



    // 请求URL
    private String url;
    // 请求参数
    private String queStr;

    private boolean emptyPackage = false;

    private final String CRLF = "\r\n";

    private Map<String, List<String>> parameterMap;

    public String getRequestInfo() {
        return requestInfo;
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getQueStr() {
        return queStr;
    }
    public boolean isEmptyPackage() {
        return emptyPackage;
    }

    public Request(InputStream is) {
        parameterMap = new HashMap<>();

        byte[] data = new byte[1024 * 1024];
        int len = 0;
        try {

            len = is.read(data);
            if (len == -1) {
                emptyPackage = true;
                return;
            }
//            System.out.println(len);
//            System.out.println("----1.获得协议------");
//            System.out.println("------读取data-----");
            requestInfo = new String(data, 0, len);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        // 分解字符串
        parseRequestInfo();

    }

    private String decode(String value, String enc) {
        try {
            return URLDecoder.decode(value, enc);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isEmptypackage() {
        return emptyPackage;
    }

//    public void setEmptypackage(boolean emptypackage) {
//        isEmptypackage = emptypackage;
//    }

    private void parseRequestInfo() {
//        System.out.println("----2.获取请求方式------");
        method = requestInfo.substring(0, requestInfo.indexOf("/")).trim();
//        System.out.println(method);
//        System.out.println("----3.获取请求URL------");
        int startidx = requestInfo.indexOf("/") + 1;
        int endidx = requestInfo.indexOf("HTTP/");
        url = requestInfo.substring(startidx, endidx).trim();
        int queueidx = url.indexOf("?");
        if (queueidx >= 0) {
            String[] urlArray = url.split("\\?");
            url = urlArray[0];
            queStr = urlArray[1];

        }
//        System.out.println(url);
//        System.out.println("----3.获取请求参数------");

        if (method.equals("POST")) {
            String qStr = requestInfo.substring(
                    requestInfo.lastIndexOf(CRLF)
            ).trim();
            if (queStr == null) {
                queStr = qStr;
            } else {
                queStr += "&" + qStr;
            }
        }
        queStr = null == queStr ? "" : queStr;

        System.out.println(method + "->" + url + "->" + queStr);
        convertMap();
    }
    /*
   处理请求参数为Map
     */
    private void convertMap() {
        // 分割
        String[] keyValues = queStr.split("&");
        for (String queryStr : keyValues
        ) {
            // 再次分割
            String[] kv = queryStr.split("=");
            kv = Arrays.copyOf(kv, 2);
            String key = kv[0];
            String value = kv[1]==null?null:decode(kv[1],"utf-8");
            // 存储到map中
            if (!parameterMap.containsKey(key)) {
                parameterMap.put(key, new ArrayList<String>());
            }
            parameterMap.get(key).add(value);

        }
    }

    /**
     * 通过name 获得对应的多个值
     * @param key
     * @return
     */
    public String[] getParameterValues(String key) {
        List<String> values = parameterMap.get(key);
        if (values == null || values.size() < 1) {
            return null;
        }
        return values.toArray(new String[0]);
    }

    /**
     * 通过name 获取对应的一个值
     * @param key
     * @return
     */
    public String getParameter(String key) {
        String[] values = getParameterValues(key);
        return values == null ? null : values[0];
    }

    public Request(Socket client) throws IOException {
        this(client.getInputStream());
    }
}
