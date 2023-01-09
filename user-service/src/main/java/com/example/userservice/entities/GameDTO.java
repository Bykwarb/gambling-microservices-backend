package com.example.userservice.entities;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class GameDTO {
    private String gameId;
    private Long userId;
    private Game.Status status;
    private Double bet;
    private Double result;
    private LocalDateTime localDateTime;
}
