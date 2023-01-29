package com.example.walletservice.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class WalletContextInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders httpHeaders = request.getHeaders();
        httpHeaders.add(WalletContext.CORRELATION_ID, WalletContextHolder.getContext().getCorrelationId());
        httpHeaders.add(WalletContext.AUTH_TOKEN, WalletContextHolder.getContext().getAuthToken());
        return execution.execute(request, body);
    }
}
