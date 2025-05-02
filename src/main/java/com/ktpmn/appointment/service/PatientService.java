package com.ktpmn.appointment.service;

import com.ktpmn.appointment.dto.request.PatientCreateRequest;
import com.ktpmn.appointment.dto.request.StaffCreateRequest;
import com.ktpmn.appointment.dto.response.PatientCreateResponse;
import com.ktpmn.appointment.dto.response.StaffCreateResponse;
import com.ktpmn.appointment.model.Patient;

public interface PatientService {
    PatientCreateResponse createPatient(PatientCreateRequest request);

    Patient getPatientByPhoneNumber(String phoneNumber);

}
