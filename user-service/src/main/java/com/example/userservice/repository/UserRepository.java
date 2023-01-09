package com.example.userservice.repository;

import com.example.userservice.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository(value = "repo")
public interface UserRepository extends JpaRepository<UserEntity, Long> {

}
