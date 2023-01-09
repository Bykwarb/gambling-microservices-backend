package com.example.gameservice.game;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class GameDTO {
    private String gameId;
    private Long userId;
    private Result.Status status;
    private Double bet;
    private Double result;
    private LocalDateTime localDateTime;
}
