package com.github.apachefoundation.jerrymouse.processor;

import com.github.apachefoundation.jerrymouse.container.Container;
import com.github.apachefoundation.jerrymouse.container.context.Context;
import com.github.apachefoundation.jerrymouse.container.valve.wapper.SimpleWrapperValve;
import com.github.apachefoundation.jerrymouse.container.wrapper.SimpleWrapper;
import com.github.apachefoundation.jerrymouse.container.loader.SimpleLoader;
import com.github.apachefoundation.jerrymouse.container.valve.wapper.ClientIpLoggerValve;
import com.github.apachefoundation.jerrymouse.container.valve.wapper.HeaderLoggerValve;
import com.github.apachefoundation.jerrymouse.container.valve.Valve;
import com.github.apachefoundation.jerrymouse.exception.RequestInvalidException;
import com.github.apachefoundation.jerrymouse.exception.handler.ExceptionHandler;
import com.github.apachefoundation.jerrymouse.http.HttpRequest;
import com.github.apachefoundation.jerrymouse.http.HttpResponse;
import com.github.apachefoundation.jerrymouse.network.endpoint.nio.NioEndpoint;
import com.github.apachefoundation.jerrymouse.network.wrapper.nio.NioSocketWrapper;
import com.github.apachefoundation.jerrymouse.utils.SocketInputBuffer;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.apachefoundation.jerrymouse.constants.Constants.*;

/**
 * @Author: xiantang
 * @Date: 2019/5/22 16:06
 */
public class HttpProcessor {

    private HttpResponse response;
    private HttpRequest request;

    /**
    是否在处理过程中有错误
     */
    private boolean ok;

    private Logger logger = Logger.getLogger(HttpProcessor.class);
    public void process(SocketChannel socketChannel, NioSocketWrapper nioSocketWrapper) {
        ExceptionHandler exceptionHandler = new ExceptionHandler();
        SocketInputBuffer inputBuffer = null;
        NioEndpoint endpoint = nioSocketWrapper.getServer();
        SocketChannel client = nioSocketWrapper.getSocketChannel();
        ok = true;
        try {
            inputBuffer = new SocketInputBuffer(socketChannel);

            request = new HttpRequest(inputBuffer);

            response = new HttpResponse(socketChannel);
            ((HttpResponse) response).setRequest(request);
            nioSocketWrapper.setResponse(response);
            nioSocketWrapper.setRequest(request);
            //关闭之后才可以完全奖body写入
        } catch (
                IOException e) {
            ok = false;
            exceptionHandler.handle(e, nioSocketWrapper);
        } catch (
                ServletException e) {
            ok = false;
            exceptionHandler.handle(e, nioSocketWrapper);
        }
        try {
            parseRequest(inputBuffer);
        } catch (RequestInvalidException e) {
            ok = false;
            exceptionHandler.handle(e, nioSocketWrapper);
        } catch (IOException e) {
            ok = false;
            exceptionHandler.handle(e, nioSocketWrapper);
        }
        logger.debug("request 构建完成"+request.getRequestURI());
        try {
            if (ok) {
            // 正则匹配
            String uri = request.getRequestURI();
            String regEx = ".*?(jpg|js|css|gif|png|ico|html)$";
            Pattern pattern = Pattern.compile(regEx);
            Matcher matcher = pattern.matcher(uri);
                if (matcher.matches()) {
                    StaticResourceProcessor srp = new StaticResourceProcessor();
                    srp.process((HttpRequest) request, (HttpResponse) response);
                } else {
                    Context context = (Context) nioSocketWrapper.getServer().getContext();
                    context.invoke(request, response);
                }
                logger.info("[" + response.getStatus() + "] " + request.getMethod() + " /" + request.getRequestURI());
                logger.debug("开始注册写事件");

                endpoint.registerToPoller(client, false, SelectionKey.OP_WRITE, nioSocketWrapper);

                logger.debug("写事件完成注册");
            }
        }
        catch (IOException e) {
            exceptionHandler.handle(e, nioSocketWrapper);
        }
        catch (ServletException e) {
            //FIXME 这里的异常处理有问题
            exceptionHandler.handle(e, nioSocketWrapper);
        }
    }

    private void parseRequest(SocketInputBuffer socketInputBuffer) throws IOException, RequestInvalidException {
        StringBuilder requestLineBuffer = new StringBuilder(1024);
        socketInputBuffer.stuffRequestBuffer(requestLineBuffer);
        String requestLine = requestLineBuffer.toString();
        String method = requestLine.substring(0, requestLine.indexOf("/")).trim();
        request.setMethod(method);
        request.setRemoteAddr(socketInputBuffer.getRemoteAddr());
        int startidx = requestLine.indexOf("/") + 1;
        int endurix = requestLine.indexOf("?");
        int endurlx = requestLine.indexOf("HTTP/");
        String requestURL = requestLine.substring(startidx, endurlx).trim();
        request.setRequestURL(requestURL);
        String protocol   = requestLine.substring(endurlx);
        request.setProtocol(protocol);
        if (endurix == -1) {
            request.setRequestURI(requestURL);
        } else {
            String requestURI = requestLine.substring(startidx, endurix).trim();
            request.setRequestURI(requestURI);
            String param      = requestLine.substring(endurix + 1, endurlx);
            request.setQueryString(param);
            // 剖析 url 参数
            String[] keyValues = param.split("&");
            for (String queryStr : keyValues) {
                String[] kv = queryStr.split("=");
                kv = Arrays.copyOf(kv, 2);
                String key = kv[0];
                String value = kv[1]==null?null: socketInputBuffer.decode(kv[1],"utf-8");
                request.setParameter(key, value);
            }
        }
        boolean flag = true;
        while (flag) {
            flag = parseRequestHeader(socketInputBuffer);
        }
    }


    private boolean parseRequestHeader(SocketInputBuffer socketInputBuffer) throws RequestInvalidException, IOException {

        StringBuilder httpHeadBuffer = new StringBuilder(1024);

        if (!socketInputBuffer.stuffRequestHeaderBuffer(httpHeadBuffer)) {
            return false;
        }
        socketInputBuffer.stuffRequestLineBuffer(httpHeadBuffer);

        String httpHead = httpHeadBuffer.toString().trim();
        String[] kv = httpHead.split(":");
        String headKey = kv[0].trim().toLowerCase();
        String headValue = kv[1].trim();
        if (CONTENT_TYPE.equals(headKey)) {
            request.setContentType(headValue);
        } else if (CONTENT_LENGTH.equals(headKey)) {
            request.setContentLength(Integer.parseInt(headValue));
        } else if (CONNECTION.equals(headKey)) {
            if (!KEEPALIVE.equals(headValue)) {
                request.setKeepAlive(false);
            }
        } else if (COOKIE.equals(headKey)) {
            headValue = headValue.replaceAll(" ", "");
            String[] cookiesStr = headValue.split(";");
            for (int i = 0; i < cookiesStr.length; i++) {
                String cookieStr = cookiesStr[i];
                String[] kav = cookieStr.split("=");
                // TODO 添加sessionid的解析
                request.addCookie(new Cookie(kav[0], kav[1]));
            }
        }
        request.setHead(headKey, headValue);
        return true;

    }
}
