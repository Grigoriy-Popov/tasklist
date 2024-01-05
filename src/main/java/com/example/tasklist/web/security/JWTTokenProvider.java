package com.example.tasklist.web.security;

import com.example.tasklist.config.properties.JWTProperties;
import com.example.tasklist.exception.AccessDeniedException;
import com.example.tasklist.exception.IncorrectTokenException;
import com.example.tasklist.model.user.Role;
import com.example.tasklist.model.user.User;
import com.example.tasklist.service.UserService;
import com.example.tasklist.web.dto.auth.JwtResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class JWTTokenProvider {

    private final JWTProperties jwtProperties;
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final Key key;

    public JWTTokenProvider(JWTProperties jwtProperties, UserDetailsService userDetailsService,
                            UserService userService) {
        this.jwtProperties = jwtProperties;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    public String createAccessToken(Long userId, String username, Set<Role> roles) {
        Instant validity = Instant.now()
                .plus(jwtProperties.getAccess(), ChronoUnit.HOURS);
        Claims claims = Jwts.claims()
                .subject(username)
                .add("id", userId)
                .add("roles", resolveRoles(roles))
                .expiration(Date.from(validity))
                .build();
        return Jwts.builder()
                .claims(claims)
                .signWith(key)
                .compact();
    }

    private List<String> resolveRoles(Set<Role> roles) {
        return roles.stream()
                .map(Enum::name)
                .toList();
    }

    public String createRefreshToken(Long userId, String username) {
        Instant validity = Instant.now()
                .plus(jwtProperties.getAccess(), ChronoUnit.HOURS);
        Claims claims = Jwts.claims()
                .subject(username)
                .add("id", userId)
                .expiration(Date.from(validity))
                .build();
        return Jwts.builder()
                .claims(claims)
                .signWith(key)
                .compact();
    }

    public JwtResponse refreshUserTokens(String refreshToken) {
        if (!validateToken(refreshToken)) {
            throw new AccessDeniedException();
        }
        Long userId = getId(refreshToken);
        User user = userService.getById(userId);
        String username = user.getUsername();
        return JwtResponse.builder()
                .id(userId)
                .username(username)
                .accessToken(createAccessToken(userId, username, user.getRoles()))
                .refreshToken(createRefreshToken(userId, username))
                .build();
    }

    public Authentication getAuthentication(String token) {
        String username = getUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public boolean validateToken(String token) {
        Jws<Claims> claims = getClaims(token);
        return !claims.getPayload().getExpiration().before(new Date());
    }

    private Long getId(String token) {
        Jws<Claims> claims = getClaims(token);
        return Long.valueOf(claims.getPayload().getId());
    }

    private String getUsername(String token) {
        Jws<Claims> claims = getClaims(token);
        return claims.getPayload().getSubject();
    }

    private Jws<Claims> getClaims(String token) {
        try {
            String jws = Jwts.builder().signWith(key).compact();
            return Jwts.parser().verifyWith((SecretKey) key).build().parseSignedClaims(token, jws.getBytes());
        } catch (SignatureException e) {
            throw new IncorrectTokenException("Incorrect token");
        }
    }
}
