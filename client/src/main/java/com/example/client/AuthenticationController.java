package com.example.client;

import com.example.client.dto.LoginDto;
import com.example.client.dto.UserDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

@Controller
@Slf4j
public class AuthenticationController {


    private RestTemplate restTemplate;
    @Value("${api.host}")
    private String host;
    @GetMapping("/main")
    public String getMainPage(){
        return "main";
    }
    @GetMapping("/login")
    public String getLoginPage(Model model){
        model.addAttribute("userDto", new UserDto());
        model.addAttribute("login", new LoginDto());
        return "login";
    }

    @PostMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response, LoginDto loginDto){
        String url = host +"/v1/auth-service/auth?email=" + loginDto.getEmail() + "&password=" + loginDto.getPassword();
        restTemplate = new RestTemplate();
        String token = restTemplate.getForObject(url, String.class);
        request.getSession().setAttribute("token", token);
        response.addCookie(new Cookie("token", token));
        return "redirect:main";
    }

    @PostMapping(value = "/registration")
    public String registration(@ModelAttribute UserDto userDto, Model model){
        String url = host +"/v1/user-service/create";
        restTemplate = new RestTemplate();
        ResponseEntity<Response> response = restTemplate.postForEntity(url, userDto, Response.class);
        if (response.getBody().getMessage().equals("User already exist")){
            return "redirect:login?exist";
        }
        return "redirect:login?success";
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    private static class Response{
        private String message;
    }
}
