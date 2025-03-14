package com.project_service.controller;

import com.project_service.dto.PatientRequestDto;
import com.project_service.dto.PatientResponseDto;
import com.project_service.dto.validators.PatientValidationGroup;
import com.project_service.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.groups.Default;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients")
@Tag(name = "Patient", description = "API for managing patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    @Operation(summary = "Getting patients")
    public ResponseEntity<List<PatientResponseDto>> getPatients() {
        return ResponseEntity.ok(patientService.getPatients());
    }

    @PostMapping
    @Operation(summary = "Creating new patient")
    public ResponseEntity<PatientResponseDto> createPatient(@Validated({Default.class, PatientValidationGroup.class}) @RequestBody PatientRequestDto patientRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.createPatient(patientRequestDto));
    }


    @PutMapping("/{id}")
    @Operation(summary = "Updating existing patient")
    public ResponseEntity<PatientResponseDto> updatePatient(
            @PathVariable UUID id, @Validated({Default.class}) @RequestBody PatientRequestDto patientRequestDto
    ) {
        return ResponseEntity.ok(patientService.updatePatient(id, patientRequestDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete existing patient")
    public ResponseEntity<Void> deletePatient(@PathVariable UUID id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}