package com.ktpmn.appointment.controller;

import com.ktpmn.appointment.dto.request.CreatePatientRequest;
import com.ktpmn.appointment.dto.response.ApiResponse; // Assuming you have a generic ApiResponse
import com.ktpmn.appointment.dto.response.PatientResponse;
import com.ktpmn.appointment.dto.request.PatientCreateRequest;
import com.ktpmn.appointment.dto.request.StaffCreateRequest;
import com.ktpmn.appointment.dto.response.ApiResponse;
import com.ktpmn.appointment.dto.response.PatientCreateResponse;
import com.ktpmn.appointment.dto.response.StaffCreateResponse;
import com.ktpmn.appointment.model.Patient;
import com.ktpmn.appointment.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/patients") // Base path for patient-related endpoints
@RequiredArgsConstructor
@RequestMapping("/api/patient")
@Slf4j
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PatientController {

    PatientService patientService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Set response status to 201 Created
    public ApiResponse<PatientResponse> createPatient(@Valid @RequestBody CreatePatientRequest request) {
        Patient createdPatient = patientService.createPatient(request);
        PatientResponse responseDto = mapToPatientResponse(createdPatient);

        // Assuming ApiResponse is a generic wrapper like: ApiResponse.<PatientResponse>builder().result(responseDto).build();
        // Adjust according to your actual ApiResponse structure
        ApiResponse<PatientResponse> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Patient created successfully"); // Or similar success message
        apiResponse.setResult(responseDto);
        // apiResponse.setCode(HttpStatus.CREATED.value()); // Optional: set code if needed

        return apiResponse;
    }

    // Helper method to map Patient entity to PatientResponse DTO
    private PatientResponse mapToPatientResponse(Patient patient) {
        return PatientResponse.builder()
                .id(patient.getId())
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .createdAt(patient.getCreatedAt()) // Assuming Audit fields are accessible
                .updatedAt(patient.getUpdatedAt()) // Assuming Audit fields are accessible
                // Map other fields as needed
                .build();
    }

}
