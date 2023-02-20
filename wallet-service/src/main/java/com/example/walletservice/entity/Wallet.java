package com.example.walletservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Entity
@ToString
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long walletId;
    @Column(unique = true)
    private String userName;
    private Double value;
    public Wallet(){}
    public Wallet(String userName){
        this.userName = userName;
        this.value = 0.0;
    }
}
