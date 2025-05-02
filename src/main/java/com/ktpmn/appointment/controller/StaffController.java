package com.ktpmn.appointment.controller;

import com.ktpmn.appointment.dto.request.CreateStaffRequest;
import com.ktpmn.appointment.dto.response.ApiResponse; // Use your existing ApiResponse
import com.ktpmn.appointment.dto.response.ListResponse;
import com.ktpmn.appointment.dto.response.StaffResponse;
import com.ktpmn.appointment.model.Staff; // Import Staff model
import com.ktpmn.appointment.service.StaffService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/staff") // Base path for staff endpoints
@Slf4j
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StaffController {

    StaffService staffService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<StaffResponse> createStaff(@Valid @RequestBody CreateStaffRequest request) {
        Staff createdStaff = staffService.createStaff(request);
        StaffResponse responseDto = mapToStaffResponse(createdStaff);

        ApiResponse<StaffResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Staff created successfully");
        apiResponse.setResult(responseDto);
        // apiResponse.setCode(HttpStatus.CREATED.value()); // Optional

        return apiResponse;
    }

    @GetMapping("/doctor")
    public ApiResponse<ListResponse<StaffResponse>> getAllDoctors(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int limit) {

        Pageable pageable = (Pageable) PageRequest.of(page, limit);

        return ApiResponse.<ListResponse<StaffResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("List of doctors")
                .result(staffService.getAllDoctors(pageable))
                .build();
    }

    // Helper method to map Staff entity to StaffResponse DTO
    private StaffResponse mapToStaffResponse(Staff staff) {
        return StaffResponse.builder()
                .id(staff.getId())
                .firstName(staff.getFirstName())
                .lastName(staff.getLastName())
                .email(staff.getEmail())
                .phoneNumber(staff.getPhoneNumber())
                .role(staff.getRole())
                .dob(staff.getDob())
                .certificationId(staff.getCertificationId())
                .sex(staff.getSex())
                .citizenId(staff.getCitizenId())
                .createdAt(staff.getCreatedAt()) // Assuming Audit fields are accessible
                .updatedAt(staff.getUpdatedAt()) // Assuming Audit fields are accessible
                // Map other fields as needed
                .build();
    }

}