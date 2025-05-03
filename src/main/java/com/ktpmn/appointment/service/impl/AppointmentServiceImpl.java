package com.ktpmn.appointment.service.impl;

import com.ktpmn.appointment.constant.AppointmentStatus;
import com.ktpmn.appointment.dto.request.CreateAppointmentRequest;
import com.ktpmn.appointment.dto.request.UpdateAppointmentRequest; // Import Update DTO
import com.ktpmn.appointment.dto.request.UpdateAppointmentStatusRequest; // Import new DTO
import com.ktpmn.appointment.exception.*;
import com.ktpmn.appointment.exception.DoctorAppointmentMismatchException; // Import custom exception

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors; // Import Collectors

import com.ktpmn.appointment.dto.request.AppointmentCreateRequest;
import com.ktpmn.appointment.dto.response.AppointmentCreateResponse;
import com.ktpmn.appointment.dto.response.AppointmentResponse;

import com.ktpmn.appointment.dto.response.ListResponse;
import com.ktpmn.appointment.dto.response.PatientResponse;
import com.ktpmn.appointment.dto.response.StaffResponse;
// import com.ktpmn.appointment.mapper.AppointmentMapper; // Remove MapStruct mapper import
import com.ktpmn.appointment.model.Patient;
import com.ktpmn.appointment.model.Staff;
import com.ktpmn.appointment.repository.PatientRepository;
import com.ktpmn.appointment.repository.StaffRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ktpmn.appointment.model.Appointment;

import com.ktpmn.appointment.model.Shift;

import com.ktpmn.appointment.repository.AppointmentRepository;

import com.ktpmn.appointment.repository.ShiftRepository;

import com.ktpmn.appointment.service.AppointmentService;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppointmentServiceImpl implements AppointmentService {

    AppointmentRepository appointmentRepository;
    PatientRepository patientRepository;
    StaffRepository staffRepository;
    ShiftRepository shiftRepository;

    @Override
    @Transactional
    public Appointment createAppointment(CreateAppointmentRequest request) {
        if (request.getToDate().isBefore(request.getFromDate())) {
            throw new IllegalArgumentException("Appointment 'toDate' must be after 'fromDate'.");
        }

        // This may be replaced with a feign client called to patient service !!?
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient", request.getPatientId().toString()));

        // This may be replaced with a feign client called to staff service !!?
        Staff doctor = staffRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Staff (Doctor)", request.getDoctorId().toString()));

        // --- Patient Check ---
        if (appointmentRepository.existsOverlappingAppointmentForPatient(request.getPatientId(), request.getFromDate(),
                request.getToDate())) {
            throw new PatientHasExistingAppointmentException(
                    "Patient already has an overlapping appointment scheduled.");
        }

        LocalTime requestedStartTime = request.getFromDate().toLocalTime();
        LocalTime requestedEndTime = request.getToDate().toLocalTime();

        List<Shift> doctorShifts = shiftRepository.findByStaffId(doctor.getId());
        boolean worksDuringRequestedTime = doctorShifts.stream()
                .anyMatch(shift -> !requestedStartTime.isBefore(shift.getFromTime())
                        && !requestedEndTime.isAfter(shift.getToTime()));

        if (!worksDuringRequestedTime) {
            throw new DoctorNotWorkingException("Doctor is not scheduled to work during the requested time slot.");
        }

        if (appointmentRepository.existsOverlappingAppointmentForDoctor(request.getDoctorId(), request.getFromDate(),
                request.getToDate())) {
            throw new DoctorTimeSlotUnavailableException(
                    "Doctor already has an overlapping appointment scheduled at this time.");
        }

        Appointment newAppointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .appointmentType(request.getAppointmentType())
                .description(request.getDescription())
                .fromDate(request.getFromDate())
                .toDate(request.getToDate())
                .appointmentStatus(AppointmentStatus.WAITING)
                .build();

        return appointmentRepository.save(newAppointment);
    }

    @Override
    public ListResponse<AppointmentResponse> getAllAppointmentsByDoctorId(UUID doctorId, Pageable pageable) {
        Page<Appointment> resultPage = appointmentRepository.findByDoctorId(doctorId, pageable);

        List<AppointmentResponse> appointmentResponses = resultPage.getContent().stream()
                .map(this::mapAppointmentToAppointmentResponse) // Use helper method for mapping
                .collect(Collectors.toList());

        return ListResponse.<AppointmentResponse>builder()
                .listData(appointmentResponses)
                .pageNumber(resultPage.getPageable().getPageNumber())
                .numberOfElements(resultPage.getNumberOfElements())
                .build();
    }

    @Override
    @Transactional
    public Appointment updateAppointment(UUID id, UpdateAppointmentRequest request) {
        // --- Fetch Existing Appointment ---
        Appointment existingAppointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", id.toString()));

        // --- Validation ---
        if (request.getToDate().isBefore(request.getFromDate())) {
            throw new IllegalArgumentException("Appointment 'toDate' must be after 'fromDate'.");
        }

        // --- Check for Conflicts IF time/date is changing ---
        boolean timeChanged = !existingAppointment.getFromDate().isEqual(request.getFromDate()) ||
                !existingAppointment.getToDate().isEqual(request.getToDate());

        if (timeChanged) {
            // Check Patient Conflict (excluding the current appointment)
            if (appointmentRepository.existsOverlappingAppointmentForPatientExcludingSelf(
                    existingAppointment.getPatient().getId(), request.getFromDate(), request.getToDate(), id)) {
                throw new PatientHasExistingAppointmentException(
                        "Patient already has another overlapping appointment scheduled at this time.");
            }

            // Check Doctor Availability (Shift)
            LocalTime requestedStartTime = request.getFromDate().toLocalTime();
            LocalTime requestedEndTime = request.getToDate().toLocalTime();
            List<Shift> doctorShifts = shiftRepository.findByStaffId(existingAppointment.getDoctor().getId());
            boolean worksDuringRequestedTime = doctorShifts.stream()
                    .anyMatch(shift -> !requestedStartTime.isBefore(shift.getFromTime())
                            && !requestedEndTime.isAfter(shift.getToTime()));
            if (!worksDuringRequestedTime) {
                throw new DoctorNotWorkingException("Doctor is not scheduled to work during the updated time slot.");
            }

            // Check Doctor Conflict (excluding the current appointment)
            if (appointmentRepository.existsOverlappingAppointmentForDoctorExcludingSelf(
                    existingAppointment.getDoctor().getId(), request.getFromDate(), request.getToDate(), id)) {
                throw new DoctorTimeSlotUnavailableException(
                        "Doctor already has another overlapping appointment scheduled at this time.");
            }
        }

        // --- Update Fields ---
        existingAppointment.setAppointmentType(request.getAppointmentType());
        existingAppointment.setDescription(request.getDescription());
        existingAppointment.setFromDate(request.getFromDate());
        existingAppointment.setToDate(request.getToDate());
        existingAppointment.setAppointmentStatus(request.getAppointmentStatus());
        // Note: patient, doctor, and ordinalNumber are generally not updated here.
        // Audit fields (createdAt, updatedAt) will be handled by @UpdateTimestamp

        // --- Save and Return ---
        return appointmentRepository.save(existingAppointment);
    }

    @Override
    @Transactional
    public void deleteAppointment(UUID id) {
        // --- Check if exists ---
        if (!appointmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Appointment", id.toString());
        }
        // --- Delete ---
        appointmentRepository.deleteById(id);
    }

    // Helper method to map Appointment to AppointmentResponse using Builder
    private AppointmentResponse mapAppointmentToAppointmentResponse(Appointment appointment) {
        // Manual mapping for nested objects (Doctor -> StaffResponse, Patient ->
        // PatientResponse)
        StaffResponse staffResponse = null;
        if (appointment.getDoctor() != null) {
            staffResponse = StaffResponse.builder()
                    .id(appointment.getDoctor().getId())
                    .firstName(appointment.getDoctor().getFirstName())
                    .lastName(appointment.getDoctor().getLastName())
                    .email(appointment.getDoctor().getEmail())
                    .phoneNumber(appointment.getDoctor().getPhoneNumber())
                    .role(appointment.getDoctor().getRole())
                    .dob(appointment.getDoctor().getDob())
                    .certificationId(appointment.getDoctor().getCertificationId())
                    .sex(appointment.getDoctor().getSex())
                    .citizenId(appointment.getDoctor().getCitizenId())
                    .createdAt(appointment.getDoctor().getCreatedAt())
                    .updatedAt(appointment.getDoctor().getUpdatedAt())
                    .build();
        }

        PatientResponse patientResponse = null;
        if (appointment.getPatient() != null) {
            patientResponse = PatientResponse.builder()
                    .id(appointment.getPatient().getId())
                    .firstName(appointment.getPatient().getFirstName())
                    .lastName(appointment.getPatient().getLastName())
                    .phoneNumber(appointment.getPatient().getPhoneNumber())
                    .createdAt(appointment.getPatient().getCreatedAt())
                    .updatedAt(appointment.getPatient().getUpdatedAt())
                    .build();
        }

        return AppointmentResponse.builder()
                .id(appointment.getId().toString())
                .doctor(staffResponse)
                .patient(patientResponse)
                .appointmentStatus(appointment.getAppointmentStatus())
                .description(appointment.getDescription())
                .appointmentType(appointment.getAppointmentType())
                .fromDate(appointment.getFromDate())
                .toDate(appointment.getToDate())
                .ordinalNumber(appointment.getOrdinalNumber())
                .build();
    }

    @Override
    @Transactional
    public Appointment updateAppointmentStatus(UpdateAppointmentStatusRequest request) {
        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", request.getAppointmentId().toString()));

        // Optional: Verify the doctor making the request matches the appointment's
        // doctor
        if (!appointment.getDoctor().getId().equals(request.getDoctorId())) {
            throw new DoctorAppointmentMismatchException("Doctor ID does not match the appointment's doctor.");
        }

        // Optional: Add logic to prevent invalid status transitions (e.g., cannot go
        // from COMPLETED back to WAITING)
        // Example:
        // if (appointment.getAppointmentStatus() == AppointmentStatus.COMPLETED ||
        // appointment.getAppointmentStatus() == AppointmentStatus.CANCELLED) {
        // throw new InvalidAppointmentStatusTransitionException("Cannot change status
        // of a completed or cancelled appointment.");
        // }

        appointment.setAppointmentStatus(request.getNewStatus());
        return appointmentRepository.save(appointment);
    }
}
