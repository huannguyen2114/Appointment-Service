package com.ktpmn.appointment.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;
import java.util.UUID;

@Data
public class CreateShiftRequest {

    @NotNull(message = "Staff ID cannot be null")
    private UUID staffId;

    @NotNull(message = "Shift name cannot be null")
    private String shiftName;

    @NotNull(message = "Start time cannot be null")
    private LocalTime startTime;

    @NotNull(message = "End time cannot be null")
    private LocalTime endTime;
}