package com.ktpmn.appointment.controller;

import java.util.List;
import java.util.UUID;

import com.ktpmn.appointment.dto.request.AppointmentCreateRequest;
import com.ktpmn.appointment.dto.response.*;
import com.ktpmn.appointment.mapper.PatientMapper;
import com.ktpmn.appointment.mapper.StaffMapper;
import com.ktpmn.appointment.model.Patient;
import com.ktpmn.appointment.service.PatientService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.ktpmn.appointment.model.Appointment;
import com.ktpmn.appointment.service.AppointmentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/appointment")
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppointmentController {
    AppointmentService appointmentService;
    PatientService patientService;
    PatientMapper patientMapper;
    StaffMapper staffMapper;

//    Get Appointment by doctor id -> /:id

    //    Get All Appointment by doctor id -> list appointment
    @GetMapping("/doctor/{id}")
    public ApiResponse<ListResponse<AppointmentResponse>> findAllAppointmentsByDoctorId(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int limit
    ) {
        Pageable pageable = (Pageable) PageRequest.of(page, limit);
        return ApiResponse.<ListResponse<AppointmentResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("List Appointment of doctor")
                .result(appointmentService.getAllAppointmentsByDoctorId(id, pageable))
                .build();
    }

    //    create doctor
    @PostMapping("/doctor")
    public ApiResponse<AppointmentCreateResponse> createAppointment(@RequestBody AppointmentCreateRequest request) {
        return ApiResponse.<AppointmentCreateResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Appointment created successfully")
                .result(appointmentService.createAppointment(request))
                .build();
    }

    //    Get Appointment by phone_number  (RequestBody: type: appointment)
    @GetMapping("/patient/{phoneNumber}")
    public ApiResponse<PatientAppointmentResponse> getAllAppointmentByPhonenumber(@PathVariable String phoneNumber) {
        PatientAppointmentResponse patientResponse = patientMapper.toPatientAppointmentResponse(patientService.getPatientByPhoneNumber(phoneNumber));
        if (patientResponse == null) {
            return ApiResponse.<PatientAppointmentResponse>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("No patient found")
                    .result(null)
                    .build();
        }
        return ApiResponse.<PatientAppointmentResponse>builder()
                .code(HttpStatus.OK.value())
                .message("All appointment by phone number")
                .result(patientResponse)
                .build();

    }

}