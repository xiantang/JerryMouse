package com.github.apachefoundation.jerrymouse.filters;

import com.github.apachefoundation.jerrymouse.http.HttpRequest;
import com.github.apachefoundation.jerrymouse.http.HttpResponse;

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
    public void doFilter(HttpRequest request, HttpResponse response) throws UnsupportedEncodingException {
        Iterator<Filter> iterator = filters.iterator();
        while (iterator.hasNext()) {
            Filter filter = iterator.next();
            filter.doFilter(request, response, this);
        }
    }
}
