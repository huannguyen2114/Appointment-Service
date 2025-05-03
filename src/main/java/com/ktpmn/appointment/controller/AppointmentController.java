package com.ktpmn.appointment.controller;

import com.ktpmn.appointment.dto.request.CreateAppointmentRequest;
import com.ktpmn.appointment.dto.request.UpdateAppointmentRequest; // Import Update DTO
import com.ktpmn.appointment.dto.request.UpdateAppointmentStatusRequest; // Import new DTO

import java.util.List; // Import List
import java.util.UUID;
import java.util.stream.Collectors; // Import Collectors

import com.ktpmn.appointment.dto.response.*;
import com.ktpmn.appointment.model.Patient; // Import Patient model
import com.ktpmn.appointment.model.Staff; // Import Staff model
import com.ktpmn.appointment.service.PatientService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.ktpmn.appointment.model.Appointment;
import com.ktpmn.appointment.service.AppointmentService;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/v1/appointments")
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppointmentController {

    AppointmentService appointmentService;
    PatientService patientService;

    // Get All Appointment by doctor id -> list appointment
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

    // Get Appointment by phone_number (RequestBody: type: appointment)
    @GetMapping("/patient/{phoneNumber}")
    public ApiResponse<PatientAppointmentResponse> getAllAppointmentByPhonenumber(@PathVariable String phoneNumber) {
        Patient patient = patientService.getPatientByPhoneNumber(phoneNumber); // Get Patient entity

        if (patient == null) {
            return ApiResponse.<PatientAppointmentResponse>builder()
                    .code(HttpStatus.NOT_FOUND.value()) // Use NOT_FOUND
                    .message("No patient found with phone number: " + phoneNumber)
                    .result(null)
                    .build();
        }

        // Manually map Appointments to AppointmentResponse DTOs
        List<AppointmentResponse> appointmentResponses = patient.getAppointments().stream()
                .map(this::mapAppointmentToResponse) // Use helper method for mapping
                .collect(Collectors.toList());

        // Manually build the PatientAppointmentResponse
        PatientAppointmentResponse patientAppointmentResponse = PatientAppointmentResponse.builder()
                .id(patient.getId())
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .phoneNumber(patient.getPhoneNumber())
                .appointments(appointmentResponses) // Set the mapped list
                .build();

        return ApiResponse.<PatientAppointmentResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Appointments found for patient with phone number: " + phoneNumber)
                .result(patientAppointmentResponse)
                .build();
    }

    @PostMapping
    public ResponseEntity<Appointment> createAppointment(@Valid @RequestBody CreateAppointmentRequest request) {
        Appointment createdAppointment = appointmentService.createAppointment(request);
        return new ResponseEntity<>(createdAppointment, HttpStatus.CREATED);
    }

    @PutMapping("/{id}") // PUT endpoint for updates
    public ResponseEntity<Appointment> updateAppointment(
            @PathVariable UUID id, // Get ID from path
            @Valid @RequestBody UpdateAppointmentRequest request) {
        Appointment updatedAppointment = appointmentService.updateAppointment(id, request);
        return ResponseEntity.ok(updatedAppointment); // Return 200 OK with updated body
    }

    @DeleteMapping("/{id}") // DELETE endpoint
    public ResponseEntity<Void> deleteAppointment(@PathVariable UUID id) { // Get ID from path
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }

    @PatchMapping("/status") // PATCH endpoint for status update
    public ResponseEntity<Appointment> updateAppointmentStatus(
            @Valid @RequestBody UpdateAppointmentStatusRequest request) {
        Appointment updatedAppointment = appointmentService.updateAppointmentStatus(request);
        return ResponseEntity.ok(updatedAppointment); // Return 200 OK with updated body
    }

    // Add other endpoints (GET) as needed

    // Helper method to map Appointment entity to AppointmentResponse DTO
    private AppointmentResponse mapAppointmentToResponse(Appointment appointment) {
        StaffResponse staffResponse = null;
        if (appointment.getDoctor() != null) {
            Staff doctor = appointment.getDoctor();
            staffResponse = StaffResponse.builder()
                    .id(doctor.getId())
                    .firstName(doctor.getFirstName())
                    .lastName(doctor.getLastName())
                    .email(doctor.getEmail())
                    .phoneNumber(doctor.getPhoneNumber())
                    .role(doctor.getRole())
                    .dob(doctor.getDob())
                    .certificationId(doctor.getCertificationId())
                    .sex(doctor.getSex())
                    .citizenId(doctor.getCitizenId())
                    .createdAt(doctor.getCreatedAt())
                    .updatedAt(doctor.getUpdatedAt())
                    .build();
        }

        PatientResponse patientResponse = null;
        if (appointment.getPatient() != null) {
            Patient apptPatient = appointment.getPatient();
            patientResponse = PatientResponse.builder()
                    .id(apptPatient.getId())
                    .firstName(apptPatient.getFirstName())
                    .lastName(apptPatient.getLastName())
                    .phoneNumber(apptPatient.getPhoneNumber())
                    .createdAt(apptPatient.getCreatedAt())
                    .updatedAt(apptPatient.getUpdatedAt())
                    .build();
        }

        return AppointmentResponse.builder()
                .id(appointment.getId().toString())
                .doctor(staffResponse)
                .patient(patientResponse) // Include mapped patient details
                .appointmentStatus(appointment.getAppointmentStatus())
                .description(appointment.getDescription())
                .appointmentType(appointment.getAppointmentType())
                .fromDate(appointment.getFromDate())
                .toDate(appointment.getToDate())
                .ordinalNumber(appointment.getOrdinalNumber())
                .build();
    }
}