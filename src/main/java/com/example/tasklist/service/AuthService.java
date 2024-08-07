package com.example.tasklist.service;

import com.example.tasklist.model.user.User;
import com.example.tasklist.web.dto.auth.JwtRequest;
import com.example.tasklist.web.dto.auth.JwtResponse;
import com.example.tasklist.web.security.JWTTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JWTTokenProvider jwtTokenProvider;

    public JwtResponse login(JwtRequest loginRequest) {
        String username = loginRequest.getUsername();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, loginRequest.getPassword()));
        User user = userService.getByUsername(username);
        long userId = user.getId();
        return JwtResponse.builder()
                .id(userId)
                .username(username)
                .accessToken(jwtTokenProvider.createAccessToken(userId, username, user.getRoles()))
                .refreshToken(jwtTokenProvider.createRefreshToken(userId, username))
                .build();
    }

    public JwtResponse refresh(String refreshToken) {
        return jwtTokenProvider.refreshUserTokens(refreshToken);
    }

}
