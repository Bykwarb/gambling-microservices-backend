package com.example.authservice.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Optional;

@Repository("AuthUserRepo")
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    User findByUsernameAndPassword(String username, String password);
    Optional<User> findUserByEmail(String email);



}
