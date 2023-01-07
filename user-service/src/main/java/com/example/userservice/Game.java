package com.example.userservice;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Game {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(nullable = false)
    private Long userId;
    private Boolean isWin;
    private Double bet;
    private Double result;
}
