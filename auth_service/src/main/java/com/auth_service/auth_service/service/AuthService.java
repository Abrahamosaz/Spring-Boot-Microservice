package com.auth_service.auth_service.service;


import com.auth_service.auth_service.dto.loginRequestDTO;
import com.auth_service.auth_service.dto.loginResponseDTO;
import com.auth_service.auth_service.exception.InvalidCredentialException;
import com.auth_service.auth_service.exception.UnAuthorizationException;
import com.auth_service.auth_service.model.User;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserService userService,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
            ) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }


    public loginResponseDTO authenticate(loginRequestDTO loginRequestDTO) {

        Optional<String> accessToken =
                userService.findUserByEmail(loginRequestDTO.getEmail())
                        .filter(u -> passwordEncoder.matches(loginRequestDTO.getPassword(), u.getPassword()))
                        .map(new Function<User, String>() {
                            @Override
                            public String apply(User user) {
                                Map<String, Object> claims = new HashMap<>();
                                claims.put("role", user.getRole());
                                Date expireTime = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10);
                                return jwtService.generateToken(user.getEmail(), claims, expireTime);
                            }
                        });
        if (accessToken.isEmpty()) throw new InvalidCredentialException("Invalid email or password");

        return new loginResponseDTO(accessToken.get());
    }

    public boolean validateToken(String accessToken) {
        try {
            return jwtService.validateToken(accessToken);
        } catch (JwtException ex) {
            return false;
        }
    }
}
