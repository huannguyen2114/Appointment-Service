package com.ktpmn.appointment.dto.request;

import com.ktpmn.appointment.constant.AppointmentType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class CreateAppointmentRequest {

    @NotNull(message = "Patient ID cannot be null")
    private UUID patientId;

    @NotNull(message = "Doctor ID cannot be null")
    private UUID doctorId;

    @NotNull(message = "Appointment type cannot be null")
    private AppointmentType appointmentType;

    private String description;

    @NotNull(message = "Start date/time cannot be null")
    @Future(message = "Appointment start time must be in the future")
    private OffsetDateTime fromDate;

    @NotNull(message = "End date/time cannot be null")
    @Future(message = "Appointment end time must be in the future")
    private OffsetDateTime toDate;
}