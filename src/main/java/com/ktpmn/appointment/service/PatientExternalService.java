package com.ktpmn.appointment.service;

import com.ktpmn.appointment.dto.response.PatientResponse;
import java.util.Optional;
import java.util.UUID;

public interface PatientExternalService {

    /**
     * Finds patient details by ID using the external service.
     *
     * @param patientId The UUID of the patient to find.
     * @return An Optional containing the PatientResponse if found, otherwise empty.
     */
    Optional<PatientResponse> findPatientById(UUID patientId);

    Optional<PatientResponse> findPatientByPhoneNumber(String phoneNumber);
}