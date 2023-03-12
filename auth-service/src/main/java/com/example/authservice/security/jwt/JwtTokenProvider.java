package com.example.authservice.security.jwt;

import com.example.authservice.security.SecurityConstant;
import com.example.authservice.user.User;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    public JwtTokenProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }
    private final static String key = Base64.getEncoder().encodeToString(SecurityConstant.SECRET_KEY.getBytes());

    @Value("${security.key}")
    private String claimsKey ;

    public String createToken(User user){
        Claims claims = Jwts.claims().setSubject(user.getUsername());
        claims.put("email", user.getEmail());
        claims.put("role", user.getRole());
        claims.put("key", passwordEncoder.encode(claimsKey));
        Date now = new Date();
        Date validity = new Date(now.getTime() + 86400 * 1000);
        return  Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String token){
        UserDetails details = userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(details, "", details.getAuthorities());
    }

    public static String getUsername(String token){
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getSubject();
    }
    public static String getEmail(String token){
        Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        return (String) claims.get("email");
    }
    public static Claims decodeJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token).getBody();
        return claims;
    }
}
