package com.example.client.events;

import com.example.client.dto.UserDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class ChangeUserDataEvent extends ApplicationEvent {
    private UserDto userDto;
    public ChangeUserDataEvent(UserDto userDto) {
        super(userDto);
        this.userDto = userDto;
    }
}
