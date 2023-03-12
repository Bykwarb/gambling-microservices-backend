package com.example.walletservice.kafka;

import com.example.userservice.entities.GameDTO;
import com.example.walletservice.exception.WalletNotFoundException;
import com.example.walletservice.service.DepositService;
import com.example.walletservice.service.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GameMessageHandler {
    @Autowired
    private DepositService depositService;

    @KafkaListener(topics = "wallet-message")
    public void GameMessageReceiver(@Payload GameDTO gameDTO, @Header(KafkaHeaders.RECEIVED_KEY) String key) throws WalletNotFoundException {
        log.debug("Received message from game-service. User: {}. Correlation-id: {}.", gameDTO.getUserName(), key);
        depositService.depositToWalletByUserName(gameDTO.getUserName(), gameDTO.getResult());
    }
}
