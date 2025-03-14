package com.project_service.mapper;

import com.project_service.dto.PatientRequestDto;
import com.project_service.dto.PatientResponseDto;
import com.project_service.model.Patient;

import java.time.LocalDate;

public class PatientMapper {

    public static PatientResponseDto toDto(Patient patient) {
        PatientResponseDto patientDto = new PatientResponseDto();
        patientDto.setId(patient.getId().toString());
        patientDto.setEmail(patient.getEmail());
        patientDto.setName(patient.getName());
        patientDto.setDateOfBirth(patient.getDateOfBirth().toString());
        patientDto.setAddress(patient.getAddress());
        return patientDto;
    }


    public static Patient toModel(PatientRequestDto patientRequestDto) {
        Patient patient = new Patient();
        patient.setName(patientRequestDto.getName());
        patient.setEmail(patientRequestDto.getEmail());
        patient.setAddress(patientRequestDto.getAddress());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDto.getDateOfBirth()));
        patient.setRegisteredDate(LocalDate.parse(patientRequestDto.getRegisteredDate()));
        return patient;
    }
}
