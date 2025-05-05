package com.ktpmn.appointment.controller;

import com.ktpmn.appointment.dto.request.CreateShiftRequest;
import com.ktpmn.appointment.dto.response.ApiResponse;
import com.ktpmn.appointment.dto.response.ShiftResponse;
import com.ktpmn.appointment.mapper.ShiftMapper; // Import the new mapper
import com.ktpmn.appointment.model.Shift;
import com.ktpmn.appointment.service.ShiftService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/shifts")
@RequiredArgsConstructor
public class ShiftController {

    private final ShiftService shiftService;
    private final ShiftMapper shiftMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ShiftResponse> createShift(@Valid @RequestBody CreateShiftRequest request) {
        Shift createdShift = shiftService.createShift(request);

        ShiftResponse responseDto = shiftMapper.mapToShiftResponse(createdShift);

        ApiResponse<ShiftResponse> apiResponse = new ApiResponse<>();
        apiResponse.setCode(HttpStatus.CREATED.value());
        apiResponse.setMessage("Shift created successfully");
        apiResponse.setResult(responseDto);

        return apiResponse;
    }

}