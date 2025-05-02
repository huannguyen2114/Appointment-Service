package com.ktpmn.appointment.repository;

import com.ktpmn.appointment.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PatientRepository extends JpaRepository<Patient, UUID> {

    Patient findByPhoneNumber(String phoneNumber);

}
