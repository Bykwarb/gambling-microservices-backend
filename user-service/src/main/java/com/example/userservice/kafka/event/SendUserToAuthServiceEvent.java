package com.example.userservice.kafka.event;

import com.example.userservice.entities.UserDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class SendUserToAuthServiceEvent extends ApplicationEvent {
    private UserDTO userDTO;

    public SendUserToAuthServiceEvent(UserDTO userDTO) {
        super(userDTO);
        this.userDTO = userDTO;
    }
}
