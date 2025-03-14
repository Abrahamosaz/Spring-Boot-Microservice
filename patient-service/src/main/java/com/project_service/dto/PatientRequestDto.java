package com.project_service.dto;


import com.project_service.dto.validators.PatientValidationGroup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PatientRequestDto {
    @NotBlank(message = "Name is required")
    private  String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email address")
    private String email;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Date of birth is required")
    private String dateOfBirth;

    @NotBlank(groups = {PatientValidationGroup.class}, message = "Registered date is required")
    private String registeredDate;
}
