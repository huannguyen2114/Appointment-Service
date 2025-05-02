package com.ktpmn.appointment.service.impl;

import com.ktpmn.appointment.dto.request.CreatePatientRequest;
import com.ktpmn.appointment.model.Patient;
import com.ktpmn.appointment.repository.PatientRepository;
import com.ktpmn.appointment.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    @Override
    @Transactional
    public Patient createPatient(CreatePatientRequest request) {
        // Optional: Add validation or checks here, e.g., check if patient already exists

        Patient newPatient = Patient.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                // Set other fields from the request if they exist
                .build();

        return patientRepository.save(newPatient);
    }

    // Implement other methods from PatientService interface here
}