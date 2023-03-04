package com.example.authservice;

import com.example.authservice.user.User;
import com.example.authservice.user.UserRepository;
import com.example.authservice.user.UserService;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.integration.handler.advice.RequestHandlerCircuitBreakerAdvice;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void testGetUserByEmailWithExistingEmail() {
        // Arrange
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));

        // Act
        User result = userService.getUserByEmail(email);

        // Assert
    }

    @Test
    public void testGetUserByEmailWithNonExistingEmail() {
        // Arrange
        String email = "test@example.com";
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.empty());

        // Act and assert
        assertThatThrownBy(() -> userService.getUserByEmail(email))
                .isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    public void testGetUserByEmailWithFallback() {
        // Arrange
        String email = "test@example.com";
        when(userRepository.findUserByEmail(email)).thenReturn(null);

        // Act
        User result = userService.getUserByEmail(email);


    }

    @Test
    public void testGetUserByEmailWithCircuitBreaker() {
        // Arrange
        String email = "test@example.com";
        when(userRepository.findUserByEmail(email)).thenThrow(new RuntimeException("error"));

        // Act and assert
        for (int i = 0; i < 5; i++) {
            assertThatThrownBy(() -> userService.getUserByEmail(email))
                    .isInstanceOf(RuntimeException.class);
        }
        assertThatThrownBy(() -> userService.getUserByEmail(email))
                .isInstanceOf(RequestHandlerCircuitBreakerAdvice.CircuitBreakerOpenException.class);

        // Wait for circuit breaker to reset
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Act
        User result = userService.getUserByEmail(email);

        // Assert

    }
}
