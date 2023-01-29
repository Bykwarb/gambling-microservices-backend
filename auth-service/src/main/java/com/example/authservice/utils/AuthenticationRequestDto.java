package com.example.authservice.utils;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class AuthenticationRequestDto {
    private String email;
    private String password;
}
