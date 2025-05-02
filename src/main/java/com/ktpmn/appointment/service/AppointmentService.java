package com.ktpmn.appointment.service;

import java.util.UUID;

import com.ktpmn.appointment.dto.request.AppointmentCreateRequest;
import com.ktpmn.appointment.dto.response.AppointmentCreateResponse;
import com.ktpmn.appointment.dto.response.AppointmentResponse;
import com.ktpmn.appointment.dto.response.ListResponse;
import com.ktpmn.appointment.dto.request.CreateAppointmentRequest;
import com.ktpmn.appointment.dto.request.UpdateAppointmentRequest; // Import Update DTO
import com.ktpmn.appointment.dto.request.UpdateAppointmentStatusRequest; // Import new DTO
import com.ktpmn.appointment.model.Appointment;
import org.springframework.data.domain.Pageable;

public interface AppointmentService {
    ListResponse<AppointmentResponse> getAllAppointmentsByDoctorId(UUID doctorId, Pageable pageable);

    AppointmentCreateResponse createAppointment(AppointmentCreateRequest appointment);

    Appointment createAppointment(CreateAppointmentRequest request);

    // Add update method signature
    Appointment updateAppointment(UUID id, UpdateAppointmentRequest request);

    // Add delete method signature
    void deleteAppointment(UUID id);

    // Add method signature for updating status
    Appointment updateAppointmentStatus(UpdateAppointmentStatusRequest request);
}
