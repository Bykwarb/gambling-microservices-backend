package com.example.userservice.event;

import com.example.userservice.entities.UserDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class ChangeUserDataEvent extends ApplicationEvent {
    private UserDTO userDTO;

    public ChangeUserDataEvent(UserDTO userDTO) {
        super(userDTO);
        this.userDTO = userDTO;
    }
}
