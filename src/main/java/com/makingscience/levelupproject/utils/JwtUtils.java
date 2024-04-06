package com.makingscience.levelupproject.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.makingscience.levelupproject.model.enums.UserStatus;
import com.makingscience.levelupproject.model.token.AuthResponse;
import com.makingscience.levelupproject.model.token.JwtAuthenticationToken;
import com.makingscience.levelupproject.repository.UserRepository;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.security.Key;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtUtils {

    private final UserRepository userRepository;

    @Value("${util.jwt.secret}")
    private String jwtSecret;

    @Value("${util.jwt.expiration}")
    private String expiration;

    public AuthResponse generateJwtToken(User user) {
        log.info("Generating JWT token for user with ID : {}" , user.getUsername());

        List<GrantedAuthority> roles = user.getAuthorities().stream().toList();
        Map<String,Object> claims = new HashMap<>();

        claims.put("roles",roles.get(0).getAuthority());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND,Integer.parseInt(expiration));
        Date current = new Date();
        Date expirationDate = calendar.getTime();
        JwtBuilder jwtBuilder = Jwts.builder()
                .setSubject(user.getUsername().toString())
                .setIssuedAt(current)
                .setExpiration(expirationDate)
                .addClaims(claims)
                .signWith(key());
        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(jwtBuilder.compact());
        authResponse.setTokenType("Bearer");
        authResponse.setExpiresIn(Integer.parseInt(expiration));
        return authResponse;
    }
    public Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public boolean validateJwt(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;

        } catch (Exception e) {
            log.error("Error during validating the token");
        }
        return false;
    }

    public String getAuthenticatedUserEmail(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public com.makingscience.levelupproject.model.entities.postgre.User getAuthenticatedUser() {
        JwtAuthenticationToken auth = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        DecodedJWT jwt = JWT.decode(auth.getToken());
        return userRepository.getByEmailAndStatus(jwt.getSubject(), UserStatus.ACTIVE).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND,"Error during fetching authenticating user!"));

    }

}
