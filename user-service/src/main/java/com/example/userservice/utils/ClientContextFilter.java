package com.example.userservice.utils;

import com.example.userservice.security.jwt.JwtTokenProvider;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ClientContextFilter implements Filter {
    private Logger logger = LoggerFactory.getLogger(ClientContextFilter.class);
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String token = httpServletRequest.getHeader(ClientContext.AUTH_TOKEN);
        if (token != null){
            ClientContextHolder.getContext().setAuthToken(token);
            ClientContextHolder.getContext().setUserName(jwtTokenProvider.getUsername(token));
        }
        ClientContextHolder.getContext().setCorrelationId(httpServletRequest.getHeader(ClientContext.CORRELATION_ID));
        filterChain.doFilter(httpServletRequest, servletResponse);
     }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
