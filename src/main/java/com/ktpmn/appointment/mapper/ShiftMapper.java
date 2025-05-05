package com.ktpmn.appointment.mapper;

import com.ktpmn.appointment.dto.response.ShiftResponse;
import com.ktpmn.appointment.model.Shift;
import org.springframework.stereotype.Component;

@Component
public class ShiftMapper {

    public ShiftResponse mapToShiftResponse(Shift shift) {
        if (shift == null) {
            return null;
        }
        return ShiftResponse.builder()
                .id(shift.getId())
                .doctorId(shift.getDoctorId())
                .startTime(shift.getFromTime())
                .endTime(shift.getToTime())
                .createdAt(shift.getCreatedAt())
                .updatedAt(shift.getUpdatedAt())
                .build();
    }
}