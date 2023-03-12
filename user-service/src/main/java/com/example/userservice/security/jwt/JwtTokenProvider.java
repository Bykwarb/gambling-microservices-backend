package com.example.userservice.security.jwt;

import com.example.authservice.security.SecurityConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Base64;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {
    @Value("${security.key}")
    private String claimsKey ;
    private final PasswordEncoder passwordEncoder;
    public JwtTokenProvider(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    private final static String key = Base64.getEncoder().encodeToString(SecurityConstant.SECRET_KEY.getBytes());

    public boolean validateToken(String token) throws JwtException, IllegalArgumentException {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            String key = claimsJws.getBody().get("key", String.class);
            if (!passwordEncoder.matches(claimsKey, key)){
                return false;
            }
            return !claimsJws.getBody().getExpiration().before(new Date());
        }catch (Exception e){
            log.debug("Provider");
            return false;
        }
    }

    public String getUsername(String token){
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getSubject();
    }
    public String resolveToken(HttpServletRequest request){
        return request.getHeader(SecurityConstant.JWT_HEADER);
    }

    public  Claims getClaims(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token).getBody();
        return claims;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> jwtExpHandler(){
        return ResponseEntity.ok("Invalid jwt token signature");
    }

}
