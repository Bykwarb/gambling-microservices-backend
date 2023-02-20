package com.example.userservice.utils;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ClientContextFilter implements Filter {
    private Logger logger = LoggerFactory.getLogger(ClientContextFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        ClientContextHolder.getContext().setCorrelationId(httpServletRequest.getHeader(ClientContext.CORRELATION_ID));
        ClientContextHolder.getContext().setAuthToken(httpServletRequest.getHeader(ClientContext.AUTH_TOKEN));
        filterChain.doFilter(httpServletRequest, servletResponse);
     }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
