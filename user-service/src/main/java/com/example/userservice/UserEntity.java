package com.example.userservice;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long userId;
    @Column(name = "username")
    private String name;
    @OneToMany(mappedBy = "userId")
    private List<Game> history;

}
