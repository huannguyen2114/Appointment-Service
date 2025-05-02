package com.ktpmn.appointment.controller;


import com.ktpmn.appointment.dto.request.StaffCreateRequest;
import com.ktpmn.appointment.dto.response.ApiResponse;
import com.ktpmn.appointment.dto.response.ListResponse;
import com.ktpmn.appointment.dto.response.StaffCreateResponse;

import com.ktpmn.appointment.dto.response.StaffResponse;
import com.ktpmn.appointment.service.StaffService;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
@Slf4j
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StaffController {

    StaffService staffService;

    //    Get all doctor -> list doctor
    @PostMapping("/doctor")
    public ApiResponse<StaffCreateResponse> createDoctor(@RequestBody StaffCreateRequest request) {

        StaffCreateResponse response = staffService.createStaff(request);

        return ApiResponse.<StaffCreateResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("doctor created")
                .result(response)
                .build();
    }

    @GetMapping("/doctor")
    public ApiResponse<ListResponse<StaffResponse>> getAllDoctors(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int limit
    ) {

        Pageable pageable = (Pageable) PageRequest.of(page, limit);

        return ApiResponse.<ListResponse<StaffResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("List of doctors")
                .result(staffService.getAllDoctors(pageable))
                .build();
    }


}
