package com.ktpmn.appointment.service;

import java.util.List;
import java.util.UUID;

import com.ktpmn.appointment.dto.request.AppointmentCreateRequest;
import com.ktpmn.appointment.dto.response.AppointmentCreateResponse;
import com.ktpmn.appointment.dto.response.AppointmentResponse;
import com.ktpmn.appointment.dto.response.ListAppointmentResponse;
import com.ktpmn.appointment.dto.response.ListResponse;
import com.ktpmn.appointment.model.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AppointmentService {
    ListResponse<AppointmentResponse> getAllAppointmentsByDoctorId(UUID doctorId, Pageable pageable);

    AppointmentCreateResponse createAppointment(AppointmentCreateRequest appointment);

}
