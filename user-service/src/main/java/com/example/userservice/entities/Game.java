package com.example.userservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;

@Entity(name = "GAME")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Game implements Persistable<String> {
    @Id
    @Column(name = "id", nullable = false)
    private String gameId;
    private Status status;
    @JoinColumn(name="userId", nullable=false)
    private Long userId;
    private Double bet;
    private Double result;
    private LocalDateTime date;

    @Transient
    @JsonIgnore
    private boolean isNew = true;
    @JsonIgnore
    @Override
    public String getId() {
        return gameId;
    }
    @Override
    public boolean isNew() {
        return isNew;
    }
    @PrePersist
    @PostLoad
    void markNotNew(){
        this.isNew = false;
    }

    public enum Status{
        Win,
        Lose,
        NotEnoughMoney,
        IncorrectLinesNumber
    }

    @Override
    public String toString() {
        return "Game{" +
                "gameId='" + gameId + '\'' +
                ", status=" + status +
                ", userId=" + userId +
                ", bet=" + bet +
                ", result=" + result +
                ", date=" + date +
                '}';
    }
}
