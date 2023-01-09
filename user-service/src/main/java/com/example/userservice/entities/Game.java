package com.example.userservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name = "GAME")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Game {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String gameId;
    private Status status;
    @JoinColumn(name="userId", nullable=false)
    private Long userId;
    private Double bet;
    private Double result;
    private LocalDateTime localDateTime;
    public enum Status{
        Win,
        Lose,
        NotEnoughMoney,
        IncorrectLinesNumber
    }
}
