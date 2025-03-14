package com.project_service.service;


import billing.BillingResponse;
import com.project_service.dto.PatientRequestDto;
import com.project_service.dto.PatientResponseDto;
import com.project_service.exception.EmailAlreadyExistsException;
import com.project_service.exception.PatientNotFoundException;
import com.project_service.grpc.BillingServiceGrpcClient;
import com.project_service.kafka.PatientProducer;
import com.project_service.mapper.PatientMapper;
import com.project_service.model.Patient;
import com.project_service.repository.PatientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {


    private static final Logger log = LoggerFactory.getLogger(PatientService.class);
    private final PatientRepository patientRepository;
    private final BillingServiceGrpcClient billingServiceGrpcClient;
    private final PatientProducer patientProducer;

    @Autowired
    public PatientService(
            PatientRepository patientRepository,
            BillingServiceGrpcClient billingServiceGrpcClient,
            PatientProducer patientProducer
    ) {
        this.patientRepository = patientRepository;
        this.billingServiceGrpcClient = billingServiceGrpcClient;
        this.patientProducer = patientProducer;
    }

    public List<PatientResponseDto> getPatients() {
        List<Patient> patients = patientRepository.findAll();
        return patients.stream().map(PatientMapper::toDto).toList();
    }

    public PatientResponseDto createPatient(PatientRequestDto patientRequestDto) {
        if (patientRepository.existsByEmail(patientRequestDto.getEmail())) {
            throw new EmailAlreadyExistsException("A patient with this email already exists");
        }

        Patient newPatient = patientRepository.save(PatientMapper.toModel(patientRequestDto));

        BillingResponse response = billingServiceGrpcClient.createBillingAccount(newPatient.getId().toString(),newPatient.getName(), newPatient.getEmail());

        log.info("Received response from billing service grpc endpoint {}", response.toString());

        this.patientProducer.sendEvent(newPatient);
        return PatientMapper.toDto(newPatient);
    }

    public PatientResponseDto updatePatient(UUID id, PatientRequestDto patientRequestDto) {

        Patient patient = patientRepository.findById(id).orElseThrow(
                () -> new PatientNotFoundException("Patient not found with ID: {} " + id)
        );

        if (patientRepository.existsByEmailAndIdNot(patientRequestDto.getEmail(), id)) {
            throw new EmailAlreadyExistsException("A patient with this email already exists");
        }

        patient.setName(patientRequestDto.getName());
        patient.setEmail(patientRequestDto.getEmail());
        patient.setAddress(patientRequestDto.getAddress());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDto.getDateOfBirth()));

        Patient updatedPatient = patientRepository.save(patient);
        return PatientMapper.toDto(updatedPatient);

    }

    public void deletePatient(UUID id) {
        patientRepository.deleteById(id);
    }
}
