package com.example.userservice.event;

import com.example.userservice.entities.UserDTO;
import com.example.userservice.utils.ClientContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserEventListener implements ApplicationListener<SendUserToAuthServiceEvent> {
    @Autowired
    private KafkaTemplate<String, UserDTO> kafkaTemplate;
    private String topicName = "user-message";
    @Override
    public void onApplicationEvent(SendUserToAuthServiceEvent event) {
        UserDTO userDTO = event.getUserDTO();
        log.debug("Sending message to Kafka. UserDTO: {}. Correlation-id: {}", userDTO, ClientContextHolder.getContext().getCorrelationId());
        kafkaTemplate.send(topicName,ClientContextHolder.getContext().getCorrelationId(), userDTO);
    }
}
