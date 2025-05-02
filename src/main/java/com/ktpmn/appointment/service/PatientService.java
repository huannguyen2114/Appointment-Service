
package com.ktpmn.appointment.service;

import com.ktpmn.appointment.dto.request.CreatePatientRequest;
import com.ktpmn.appointment.model.Patient; // Import Patient model

public interface PatientService {
    Patient createPatient(CreatePatientRequest request);

    // Add other patient-related service methods here (e.g., getPatientById,
    // updatePatient, deletePatient)
    Patient getPatientByPhoneNumber(String phoneNumber);
}