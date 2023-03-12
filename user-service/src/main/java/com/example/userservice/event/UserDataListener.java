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
public class UserDataListener implements ApplicationListener<ChangeUserDataEvent> {
    private String topicName = "user-data";
    @Autowired
    private KafkaTemplate<String, UserDTO> kafkaTemplate;
    @Override
    public void onApplicationEvent(ChangeUserDataEvent event) {
        UserDTO userDTO = event.getUserDTO();
        log.debug("Sending message to Kafka. UserDTO: {}. Correlation-id: {}", userDTO, ClientContextHolder.getContext().getCorrelationId());
        kafkaTemplate.send(topicName,ClientContextHolder.getContext().getCorrelationId(), userDTO);
    }
}
