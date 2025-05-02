package com.ktpmn.appointment.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
public class ShiftResponse {
    private UUID id;
    private UUID staffId; // Include staff ID
    private String staffFirstName; // Example: Include staff name
    private String staffLastName; // Example: Include staff name
    private LocalDate shiftDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    // Add other fields from the Shift entity you want to expose
}