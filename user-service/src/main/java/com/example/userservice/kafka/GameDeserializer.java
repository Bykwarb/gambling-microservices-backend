package com.example.userservice.kafka;

import com.example.userservice.entities.GameDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;

public class GameDeserializer implements Deserializer<GameDTO> {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public GameDTO deserialize(String s, byte[] bytes) {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try {
            if (bytes == null){
                return null;
            }
            return objectMapper.readValue(bytes, GameDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
