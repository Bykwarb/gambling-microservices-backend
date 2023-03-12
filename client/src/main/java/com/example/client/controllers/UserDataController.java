package com.example.client.controllers;

import com.example.client.dto.UserDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Controller
@Slf4j
public class UserDataController {
    private RestTemplate restTemplate;
    @Value("${api.host}")
    private String host;
    @GetMapping("/user")
    public String getUserPage(Model model, HttpServletRequest request){
        UserDto userDto = new UserDto();
        model.addAttribute("servletRequest", request);
        model.addAttribute("userDto", userDto);
        return "user";
    }
    @PostMapping("/change-userdata")
    public String changeUserData(@ModelAttribute UserDto userDto){
        return "user";
    }
    @PostMapping("/change-password")
    public String changePassword(){
        return "user";
    }

}
