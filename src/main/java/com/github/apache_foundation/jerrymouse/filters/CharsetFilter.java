package com.github.apache_foundation.jerrymouse.filters;

import com.github.apache_foundation.jerrymouse.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * Created By CaiTieZhu on 2019/4/22
 */
/** 字符编码过滤器 Created By CaiTieZhu **/
public class CharsetFilter implements Filter {
    private String charset;
    private String DEFAULT_CHARSET = "UTF-8";

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
}
