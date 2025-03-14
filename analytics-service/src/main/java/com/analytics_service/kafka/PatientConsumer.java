package com.analytics_service.kafka;


import com.google.protobuf.InvalidProtocolBufferException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

import java.util.Arrays;

@Service
public class PatientConsumer {

    private static final Logger log = LoggerFactory.getLogger(PatientConsumer.class);

    @KafkaListener(topics = "patient", groupId = "analytics-service")
    public void consumeEvent(byte[] event) {

        try {
            PatientEvent patientEvent = PatientEvent.parseFrom(event);

//            perform any business logic here

            log.info("Received event data from kafka broker: Event [patientId={}, name={}, email={}, eventType={}]",
                    patientEvent.getPatientId(),
                    patientEvent.getName(),
                    patientEvent.getEmail(),
                    patientEvent.getEventType()
                    );
        } catch (InvalidProtocolBufferException ex) {
            log.info("Error deserializing patient event kafka data {}", Arrays.toString(event));
        }
    }
}
