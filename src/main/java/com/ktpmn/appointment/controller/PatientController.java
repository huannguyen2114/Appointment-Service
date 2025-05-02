package com.ktpmn.appointment.controller;

import com.ktpmn.appointment.dto.request.PatientCreateRequest;
import com.ktpmn.appointment.dto.request.StaffCreateRequest;
import com.ktpmn.appointment.dto.response.ApiResponse;
import com.ktpmn.appointment.dto.response.PatientCreateResponse;
import com.ktpmn.appointment.dto.response.StaffCreateResponse;
import com.ktpmn.appointment.model.Patient;
import com.ktpmn.appointment.service.PatientService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/patient")
@Slf4j
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PatientController {

    PatientService patientService;

    @PostMapping("/create")
    public ApiResponse<PatientCreateResponse> createPatient(@RequestBody PatientCreateRequest request) {
        PatientCreateResponse response = patientService.createPatient(request);
        return ApiResponse.<PatientCreateResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Patient created")
                .result(response)
                .build();
    }

}
