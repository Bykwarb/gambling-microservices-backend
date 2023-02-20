package com.example.authservice.kafka;

import com.example.authservice.utils.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;

public class UserDtoDeserializer implements Deserializer<UserDto> {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public UserDto deserialize(String s, byte[] bytes) {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try {
            if (bytes == null){
                return null;
            }
            return objectMapper.readValue(bytes, UserDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
