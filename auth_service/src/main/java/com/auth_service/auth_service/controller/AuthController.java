package com.auth_service.auth_service.controller;


import com.auth_service.auth_service.dto.loginRequestDTO;
import com.auth_service.auth_service.dto.loginResponseDTO;
import com.auth_service.auth_service.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "Login user and get a jwt token when authenticated")
    public ResponseEntity<loginResponseDTO> login(@RequestBody() loginRequestDTO loginRequestDTO) {
        loginResponseDTO response = authService.authenticate(loginRequestDTO);
        return ResponseEntity.ok().body(response);
    }


    @GetMapping("/validate")
    @Operation(summary = "validate accessToken for authorization")
    public ResponseEntity<?> validateAccessToken(@RequestHeader("Authorization") String authorization) {

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return authService.validateToken(authorization.substring(7))
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
