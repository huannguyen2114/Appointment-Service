package com.ktpmn.appointment.controller;

import com.ktpmn.appointment.dto.request.CreateAppointmentRequest;
import com.ktpmn.appointment.dto.request.UpdateAppointmentRequest;
import com.ktpmn.appointment.dto.request.UpdateAppointmentStatusRequest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.ktpmn.appointment.dto.response.*;
import com.ktpmn.appointment.mapper.AppointmentMapper;

import com.ktpmn.appointment.repository.AppointmentRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.ktpmn.appointment.model.Appointment;
import com.ktpmn.appointment.service.AppointmentService;
import com.ktpmn.appointment.service.PatientExternalService;

import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppointmentController {

    AppointmentService appointmentService;
    PatientExternalService patientExternalService;
    AppointmentRepository appointmentRepository;
    AppointmentMapper appointmentMapper;

    @GetMapping("/doctor/{id}")
    public ApiResponse<ListResponse<AppointmentResponse>> findAllAppointmentsByDoctorId(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int limit) {
        Pageable pageable = (Pageable) PageRequest.of(page, limit);
        return ApiResponse.<ListResponse<AppointmentResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("List Appointment of doctor")
                .result(appointmentService.getAllAppointmentsByDoctorId(id, pageable))
                .build();
    }

    @GetMapping("/patient/phone/{phoneNumber}")
    public ApiResponse<PatientAppointmentResponse> getAllAppointmentByPhonenumber(@PathVariable String phoneNumber) {
        Optional<PatientResponse> patientOptional = patientExternalService.findPatientByPhoneNumber(phoneNumber);

        if (!patientOptional.isPresent()) {
            return ApiResponse.<PatientAppointmentResponse>builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message("No patient found with phone number: " + phoneNumber)
                    .result(null)
                    .build();
        }

        PatientResponse patient = patientOptional.get();
        List<Appointment> appointments = appointmentRepository.findByPatientId(patient.getId());

        // Map Appointments to AppointmentResponse DTOs using the mapper
        List<AppointmentResponse> appointmentResponses;
        if (appointments != null && !appointments.isEmpty()) {
            appointmentResponses = appointments.stream()
                    .map(appointmentMapper::mapAppointmentToAppointmentResponse)
                    .collect(Collectors.toList());
        } else {
            appointmentResponses = Collections.emptyList();
        }

        PatientAppointmentResponse patientAppointmentResponse = PatientAppointmentResponse.builder()
                .id(patient.getId())
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .phoneNumber(patient.getContactNumber())
                .appointments(appointmentResponses)
                .build();

        return ApiResponse.<PatientAppointmentResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Appointments found for patient with phone number: " + phoneNumber)
                .result(patientAppointmentResponse)
                .build();
    }

    @PostMapping
    public ResponseEntity<AppointmentResponse> createAppointment(@Valid @RequestBody CreateAppointmentRequest request) { // Return
                                                                                                                         // AppointmentResponse
        Appointment createdAppointment = appointmentService.createAppointment(request);

        AppointmentResponse response = appointmentMapper.mapAppointmentToAppointmentResponse(createdAppointment);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentResponse> updateAppointment(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateAppointmentRequest request) {
        Appointment updatedAppointment = appointmentService.updateAppointment(id, request);

        AppointmentResponse response = appointmentMapper.mapAppointmentToAppointmentResponse(updatedAppointment);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable UUID id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/status")
    public ResponseEntity<AppointmentResponse> updateAppointmentStatus(
            @Valid @RequestBody UpdateAppointmentStatusRequest request) {
        Appointment updatedAppointment = appointmentService.updateAppointmentStatus(request);
        AppointmentResponse response = appointmentMapper.mapAppointmentToAppointmentResponse(updatedAppointment);
        return ResponseEntity.ok(response);
    }
}