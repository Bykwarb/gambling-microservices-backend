package com.example.userservice.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class ClientContextInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders httpHeaders = request.getHeaders();
        log.debug("Add correlation id: {}", ClientContextHolder.getContext().getCorrelationId());
        httpHeaders.add(ClientContext.CORRELATION_ID, ClientContextHolder.getContext().getCorrelationId());
        log.debug("Add auth_token: {}", ClientContextHolder.getContext().getAuthToken());
        httpHeaders.add(ClientContext.AUTH_TOKEN, ClientContextHolder.getContext().getAuthToken());
        return execution.execute(request, body);
    }
}
