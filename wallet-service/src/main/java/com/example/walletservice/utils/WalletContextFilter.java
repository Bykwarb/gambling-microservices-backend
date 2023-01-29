package com.example.walletservice.utils;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class WalletContextFilter implements Filter {
    private Logger logger = LoggerFactory.getLogger(WalletContextFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        WalletContextHolder.getContext().setCorrelationId(httpServletRequest.getHeader(WalletContext.CORRELATION_ID));
        WalletContextHolder.getContext().setAuthToken(httpServletRequest.getHeader(WalletContext.AUTH_TOKEN));
        logger.debug("WalletContextFilter Correlation id: {}", WalletContextHolder.getContext().getCorrelationId());
        filterChain.doFilter(httpServletRequest, servletResponse);
     }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
