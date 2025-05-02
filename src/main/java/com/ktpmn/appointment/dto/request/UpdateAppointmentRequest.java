package com.ktpmn.appointment.dto.request;

import com.ktpmn.appointment.constant.AppointmentStatus;
import com.ktpmn.appointment.constant.AppointmentType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class UpdateAppointmentRequest {

    // Note: We typically don't allow changing patientId or doctorId easily via a simple update.
    // If needed, that might be a different process (e.g., cancel and rebook).
    // For now, we'll assume these are not changed here.

    @NotNull(message = "Appointment type cannot be null")
    private AppointmentType appointmentType;

    private String description; // Optional

    @NotNull(message = "Start date/time cannot be null")
    @Future(message = "Appointment start time must be in the future")
    private OffsetDateTime fromDate;

    @NotNull(message = "End date/time cannot be null")
    @Future(message = "Appointment end time must be in the future")
    private OffsetDateTime toDate;

    @NotNull(message = "Appointment status cannot be null")
    private AppointmentStatus appointmentStatus; // Allow updating status

    // Add validation: toDate must be after fromDate (can be done via custom validator)
}