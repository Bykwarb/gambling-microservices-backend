package com.example.gameservice.utils;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GameContextFilter implements Filter {
    private Logger logger = LoggerFactory.getLogger(GameContextFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        GameContextHolder.getContext().setCorrelationId(httpServletRequest.getHeader(GameContext.CORRELATION_ID));
        GameContextHolder.getContext().setAuthToken(httpServletRequest.getHeader(GameContext.AUTH_TOKEN));
        logger.debug("GameContextFilter Correlation id: {}", GameContextHolder.getContext().getCorrelationId());
        filterChain.doFilter(httpServletRequest, servletResponse);
     }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
