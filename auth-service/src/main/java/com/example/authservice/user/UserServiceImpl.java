package com.example.authservice.user;

import com.example.authservice.utils.UserRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("AuthUserService")
@ComponentScan("com.example.authservice.user")
public class UserServiceImpl implements UserService{

    @Autowired
    @Qualifier("AuthUserRepo")
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> getByUsrEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email).get();
    }

    @Override
    public void saveUser(UserRequestDto userRequestDto) {
        User user = new User();
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        user.setEmail(userRequestDto.getEmail());
        user.setUsername(userRequestDto.getUsername());
        user.setRole(Role.USER);
        user.setAccountEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        userRepository.save(user);
    }

}
