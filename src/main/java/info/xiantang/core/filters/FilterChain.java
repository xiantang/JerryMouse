package info.xiantang.core.filters;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created By CaiTieZhu on 2019/4/22
 */
public interface FilterChain {

    void doFilter(HttpServletRequest request, HttpServletResponse response);
}
