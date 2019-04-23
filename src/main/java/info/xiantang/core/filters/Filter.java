package info.xiantang.core.filters;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * Created By CaiTieZhu on 2019/4/22
 */
public interface Filter {

    void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws UnsupportedEncodingException;

    void destroy();
}
