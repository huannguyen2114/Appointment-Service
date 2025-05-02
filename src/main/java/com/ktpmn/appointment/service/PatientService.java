package com.ktpmn.appointment.service;

import com.ktpmn.appointment.dto.request.PatientCreateRequest;
import com.ktpmn.appointment.dto.request.StaffCreateRequest;
import com.ktpmn.appointment.dto.response.PatientCreateResponse;
import com.ktpmn.appointment.dto.response.StaffCreateResponse;
import com.ktpmn.appointment.model.Patient;
import com.ktpmn.appointment.dto.request.CreatePatientRequest;
import com.ktpmn.appointment.model.Patient; // Import Patient model

import java.util.UUID; // Import UUID if needed for other methods

public interface PatientService {
    Patient getPatientByPhoneNumber(String phoneNumber);

    Patient createPatient(CreatePatientRequest request);
    // Add other patient-related service methods here (e.g., getPatientById, updatePatient, deletePatient)
}