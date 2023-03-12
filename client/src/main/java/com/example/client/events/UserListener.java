package com.example.client.events;

import com.example.client.dto.UserDto;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UserListener implements ApplicationListener<ChangeUserDataEvent> {
    private RestTemplate restTemplate = new RestTemplate();
    @Override
    public void onApplicationEvent(ChangeUserDataEvent event) {
        UserDto userDto = event.getUserDto();
        //restTemplate.postForEntity();
        //restTemplate.postForEntity();
        //restTemplate.postForEntity();
    }
}
