package com.ktpmn.appointment.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ktpmn.appointment.model.Appointment;
import com.ktpmn.appointment.repository.AppointmentRepository;
import com.ktpmn.appointment.service.AppointmentService;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Service
@AllArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;

    @Override
    public List<Appointment> helString() {
        return appointmentRepository.findAll();
    }

}
