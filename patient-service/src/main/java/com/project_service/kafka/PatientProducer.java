package com.project_service.kafka;


import com.project_service.model.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@Service
public class PatientProducer {

    private static final Logger log = LoggerFactory.getLogger(PatientProducer.class);
    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public PatientProducer(KafkaTemplate<String, byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(Patient patient) {
        PatientEvent event = PatientEvent.newBuilder()
                .setPatientId(patient.getId().toString())
                .setName(patient.getName())
                .setEmail(patient.getEmail())
                .setEventType("PATIENT_CREATED")
                .build();

        try {
            this.kafkaTemplate.send("patient", event.toByteArray());
            log.info("Event sent from patient service via kafka {}", event);
        } catch (Exception ex) {
            log.info("Error sending kafka event from patient service {} {}", event, ex.getMessage());
        }

    }
}
