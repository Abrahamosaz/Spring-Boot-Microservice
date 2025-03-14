package com.project_service.dto;


import lombok.*;

//
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PatientResponseDto {
    private String id;
    private String email;
    private String name;
    private String address;
    private String dateOfBirth;
}
