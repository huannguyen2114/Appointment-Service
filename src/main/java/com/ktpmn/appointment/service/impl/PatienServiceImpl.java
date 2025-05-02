package com.ktpmn.appointment.service.impl;

import com.ktpmn.appointment.dto.request.PatientCreateRequest;
import com.ktpmn.appointment.dto.response.PatientCreateResponse;
import com.ktpmn.appointment.mapper.PatientMapper;
import com.ktpmn.appointment.model.Patient;
import com.ktpmn.appointment.repository.PatientRepository;
import com.ktpmn.appointment.service.PatientService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PatienServiceImpl implements PatientService {

    PatientMapper patientMapper;
    PatientRepository patientRepository;

    @Override
    @Transactional
    public PatientCreateResponse createPatient(PatientCreateRequest patient) {
        Patient result = patientRepository.save(patientMapper.toPatient(patient));
        return patientMapper.toPatientCreateReponse(result);
    }

    @Override
    public Patient getPatientByPhoneNumber(String phoneNumber) {
        Patient patient = patientRepository.findByPhoneNumber(phoneNumber);
//        System.out.println(patient);
        return patient;
    }


}
