package com.ktpmn.appointment.controller;

import com.ktpmn.appointment.dto.request.CreateShiftRequest;
import com.ktpmn.appointment.dto.response.ApiResponse; // Use your existing ApiResponse
import com.ktpmn.appointment.dto.response.ShiftResponse;
import com.ktpmn.appointment.model.Shift;
import com.ktpmn.appointment.service.ShiftService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/shifts") // Base path for shift endpoints
@RequiredArgsConstructor
public class ShiftController {

    private final ShiftService shiftService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ShiftResponse> createShift(@Valid @RequestBody CreateShiftRequest request) {
        Shift createdShift = shiftService.createShift(request);
        ShiftResponse responseDto = mapToShiftResponse(createdShift);

        ApiResponse<ShiftResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Shift created successfully");
        apiResponse.setResult(responseDto);
        // apiResponse.setCode(HttpStatus.CREATED.value()); // Optional

        return apiResponse;
    }

    // Helper method to map Shift entity to ShiftResponse DTO
    private ShiftResponse mapToShiftResponse(Shift shift) {
        return ShiftResponse.builder()
                .id(shift.getId())
                .staffId(shift.getStaff().getId())
                .staffFirstName(shift.getStaff().getFirstName())
                .staffLastName(shift.getStaff().getLastName())
                .startTime(shift.getFromTime())
                .endTime(shift.getToTime())
                .createdAt(shift.getCreatedAt())
                .updatedAt(shift.getUpdatedAt())
                .build();
    }
}