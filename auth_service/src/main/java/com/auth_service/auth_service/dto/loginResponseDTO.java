package com.auth_service.auth_service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
public class loginResponseDTO {
    private final String accessToken;
}
