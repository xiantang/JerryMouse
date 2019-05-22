package com.github.apachefoundation.jerrymouse.filters;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created By CaiTieZhu on 2019/4/22
 */
public class StanderFilterChain implements FilterChain {

    private List<Filter> filters = new ArrayList<>();

    public StanderFilterChain addFilter(Filter filter) {
        filters.add(filter);
        return this;
    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        Iterator<Filter> iterator = filters.iterator();
        while (iterator.hasNext()) {
            Filter filter = iterator.next();
            filter.doFilter(request, response, this);
        }
    }
}
