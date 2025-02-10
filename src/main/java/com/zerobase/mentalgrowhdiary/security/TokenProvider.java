package com.zerobase.mentalgrowhdiary.security;


import com.zerobase.mentalgrowhdiary.service.UserService;
import com.zerobase.mentalgrowhdiary.type.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenProvider {

    private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24;
    private static final String KEY_ROLE = "role";

    @Value("${spring.jwt.secret}")
    private String secretKeyEncoded;

    private final UserService userService;
    private SecretKey secretKey;



    @PostConstruct
    public void init(){

        try {
            byte[] decodedKey = Base64.getDecoder().decode(secretKeyEncoded);
            this.secretKey = Keys.hmacShaKeyFor(decodedKey);
            log.info("TokenProvider initialized success.");

        } catch (IllegalArgumentException e) {

            log.error("Failed to initialize TokenProvider : {}", secretKeyEncoded);
            throw e;
        }
    }

    public String generateToken(Long userId, String username , Role role) {

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("userId", userId);
        claims.put(KEY_ROLE, role.name());

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey,SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean validateToken(String token) {

        try{

            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);

            return true;

        } catch(JwtException e){
            log.warn("Invalid Token: ",e.getMessage());
            return false;
        }
    }

    public String getUsername(String token) {
        return parseClaims(token).getSubject();
    }

    public Long getUserId(String token) {
        Claims claims = parseClaims(token);

        return claims.get("userId", Long.class);
    }

    public Role getRole(String token) {
        Claims claims = parseClaims(token);
        return Role.valueOf(claims.get(KEY_ROLE, String.class));
    }

    public Authentication getAuthentication(String token) {
        String username = getUsername(token);

        UserDetails userDetails = userService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(userDetails,
                null, userDetails.getAuthorities());
    }

    private Claims parseClaims(String token){

        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    //키를 생성하는 메서드
    public static String generateStringSecretKey(){
        return Base64.getEncoder().encodeToString(
                Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded()
        );
    }
}
