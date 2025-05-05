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
    private UUID doctorId;
    private LocalDate shiftDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}