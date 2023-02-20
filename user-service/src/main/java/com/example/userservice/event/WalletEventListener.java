package com.example.userservice.event;

import com.example.userservice.entities.WalletDto;
import com.example.userservice.utils.ClientContextHolder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class WalletEventListener implements ApplicationListener<SendRequestToCreateWalletEvent> {

    private RestTemplate restTemplate = new RestTemplate();
    @Value("${wallet-uri}")
    private String walletUrl;
    @SneakyThrows
    @Override
    public void onApplicationEvent(SendRequestToCreateWalletEvent event) {
        WalletDto walletDto = new WalletDto(event.getUsername());
        log.debug("Outgoing request to WalletService. Username {}", event.getUsername());
        restTemplate.postForEntity(walletUrl, walletDto, String.class);
    }
}
