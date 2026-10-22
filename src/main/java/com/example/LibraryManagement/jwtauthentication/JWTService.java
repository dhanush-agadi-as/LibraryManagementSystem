package com.example.LibraryManagement.jwtauthentication;


import com.example.LibraryManagement.entity.User;
import com.example.LibraryManagement.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Service
public class JWTService {

    private final UserService userService;

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.expirationkey}")
    private Long expirationTime;

    public JWTService(UserService userService) {
        this.userService = userService;
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T>  T extractClaim(String token, Function<Claims, T> claimsResolver) {

        final Claims claims = extractAllClaims(token);

        return claimsResolver.apply(claims);

    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignInKey(){
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateToken(User user){

        return generateToken(new HashMap<>(),user);

    }

    public String generateToken(HashMap<String, Object> claims, User user){

        return Jwts
                .builder()
                .claims()
                .subject(user.getName())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .and().compact();

    }

    public boolean isTokenValid(String token , User user){

        final String username =  extractUsername(token);

        return user.getUsername().equals(username) && !isTokenExpired(token);

    }

    private boolean isTokenExpired(String token){
        return extractClaim(token,Claims::getExpiration).before(new Date());
    }



}
