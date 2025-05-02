package com.ktpmn.appointment.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.ktpmn.appointment.constant.Role;
import com.ktpmn.appointment.dto.request.AppointmentCreateRequest;
import com.ktpmn.appointment.dto.response.AppointmentCreateResponse;
import com.ktpmn.appointment.dto.response.AppointmentResponse;
import com.ktpmn.appointment.dto.response.ListAppointmentResponse;
import com.ktpmn.appointment.dto.response.ListResponse;
import com.ktpmn.appointment.mapper.AppointmentMapper;
import com.ktpmn.appointment.model.Patient;
import com.ktpmn.appointment.model.Staff;
import com.ktpmn.appointment.repository.PatientRepository;
import com.ktpmn.appointment.repository.StaffRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ktpmn.appointment.model.Appointment;
import com.ktpmn.appointment.repository.AppointmentRepository;
import com.ktpmn.appointment.service.AppointmentService;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppointmentServiceImpl implements AppointmentService {
    AppointmentRepository appointmentRepository;
    StaffRepository staffRepository;
    PatientRepository patientRepository;
    AppointmentMapper appointmentMapper;

    @Override
    public ListResponse<AppointmentResponse> getAllAppointmentsByDoctorId(UUID doctorId, Pageable pageable) {

//        Staff doctor = staffRepository.findByIdAndRole(doctorId, Role.DOCTOR);
//
//        if (doctor == null) {
//            throw
//        }

        Page<Appointment> result = appointmentRepository.findByDoctorId(doctorId, pageable);

//        System.out.println("cascascascsacascsac");

        ListResponse<AppointmentResponse> response = appointmentMapper.toListAppointmentResponse(result);

        return response;
    }

    @Override
    @Transactional
    public AppointmentCreateResponse createAppointment(AppointmentCreateRequest appointment) {

        Staff doctor = staffRepository.findById(appointment.getDoctorId()).orElse(null);

        if (doctor == null) {
            throw new IllegalArgumentException("Doctor not found");
        }

        Patient patient = patientRepository.findById(appointment.getPatientId()).orElse(null);
        if (patient == null) {
            throw new IllegalArgumentException("Patient not found");
        }


        Appointment result = appointmentRepository.save(Appointment.builder()
                .doctor(doctor)
                .patient(patient)
                .build());

        return appointmentMapper.toAppointmentCreateResponse(result);

    }

}
