package com.ktpmn.appointment.dto.request;

import com.ktpmn.appointment.constant.AppointmentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class UpdateAppointmentStatusRequest {

    @NotNull(message = "Doctor ID cannot be null")
    private UUID doctorId;

    @NotNull(message = "Appointment ID cannot be null")
    private UUID appointmentId;

    @NotNull(message = "New status cannot be null")
    private AppointmentStatus newStatus;
}