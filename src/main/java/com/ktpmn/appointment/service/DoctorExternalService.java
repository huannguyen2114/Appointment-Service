package com.ktpmn.appointment.service;

import java.util.Optional;
import java.util.UUID;

import com.ktpmn.appointment.dto.response.DoctorResponse;

public interface DoctorExternalService {
    Optional<DoctorResponse> findDoctorById(UUID doctorId);
}