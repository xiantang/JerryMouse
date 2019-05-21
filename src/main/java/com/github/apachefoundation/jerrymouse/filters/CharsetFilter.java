package com.github.apachefoundation.jerrymouse.filters;

import com.github.apachefoundation.jerrymouse.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * Created By CaiTieZhu on 2019/4/22
 */
/** 字符编码过滤器
 *
 * RFC2616 规定如何 character 没被设置的话, 默认值必须设置为 ISO-8859-1
 *
 * Created By CaiTieZhu **/
public class CharsetFilter implements Filter {
    private String charset;
    private String DEFAULT_CHARSET = "ISO-8859-1";

    /** 解析配置文件传入字符集配置 Created By CaiTieZhu **/
    public CharsetFilter(String charset) {
        this.charset = charset;
    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws UnsupportedEncodingException {
        if (StringUtils.isNullOrEmpty(charset)) {
            // 设置默认字符集为 UTF-8
            charset = DEFAULT_CHARSET;
        }
        request.setCharacterEncoding(charset);
    }

    @Override
    public void destroy() { }

    public void setCharset(String charset) {
        this.charset = charset;
    }
}
