package com.example.userservice.security.jwt;

import com.example.authservice.user.Role;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Slf4j
public class JwtTokenFilter extends GenericFilterBean {


    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) servletRequest);
        try {
            if (token != null && jwtTokenProvider.validateToken(token)){
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        jwtTokenProvider.getUsername(token),
                        "",
                        Role.valueOf(jwtTokenProvider.getClaims(token).get("role").toString()).getAuthorities());
                if (authentication != null){
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }else {
                    ((HttpServletResponse) servletResponse).sendError(HttpStatus.FORBIDDEN.value());
                    return;
                }
            }

        }catch (Exception e){
            ((HttpServletResponse) servletResponse).sendError(HttpStatus.FORBIDDEN.value());
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @ExceptionHandler(value = ExpiredJwtException.class)
    public ResponseEntity<String> jwtAuthExp(ExpiredJwtException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    }
}
