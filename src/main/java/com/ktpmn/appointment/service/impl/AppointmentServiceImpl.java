package com.ktpmn.appointment.service.impl;

import com.ktpmn.appointment.constant.AppointmentStatus;
import com.ktpmn.appointment.dto.request.CreateAppointmentRequest;
import com.ktpmn.appointment.dto.request.UpdateAppointmentRequest; // Import Update DTO
import com.ktpmn.appointment.dto.request.UpdateAppointmentStatusRequest; // Import new DTO
import com.ktpmn.appointment.exception.*;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors; // Import Collectors

import com.ktpmn.appointment.dto.response.AppointmentResponse;

import com.ktpmn.appointment.dto.response.ListResponse;
import com.ktpmn.appointment.mapper.AppointmentMapper; // Add import

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ktpmn.appointment.model.Appointment;

import com.ktpmn.appointment.model.Shift;

import com.ktpmn.appointment.repository.AppointmentRepository;

import com.ktpmn.appointment.repository.ShiftRepository;

import com.ktpmn.appointment.service.AppointmentService;

import com.ktpmn.appointment.service.PatientExternalService;

import com.ktpmn.appointment.service.DoctorExternalService;

import jakarta.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppointmentServiceImpl implements AppointmentService {

    AppointmentRepository appointmentRepository;
    ShiftRepository shiftRepository; // Keep ShiftRepository for now
    PatientExternalService patientExternalService;
    DoctorExternalService doctorExternalService; // + Add DoctorExternalService field
    AppointmentMapper appointmentMapper;

    @Override
    @Transactional
    public Appointment createAppointment(CreateAppointmentRequest request) {
        if (request.getToDate().isBefore(request.getFromDate())) {
            throw new IllegalArgumentException("Appointment 'toDate' must be after 'fromDate'.");
        }

        patientExternalService.findPatientById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient", request.getPatientId().toString()));
        log.info("Successfully validated existence of patient with ID: {}", request.getPatientId());

        doctorExternalService.findDoctorById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", request.getDoctorId().toString()));
        log.info("Successfully validated existence of doctor with ID: {}", request.getDoctorId());

        if (appointmentRepository.existsOverlappingAppointmentForPatient(request.getPatientId(), request.getFromDate(),
                request.getToDate())) {
            throw new PatientHasExistingAppointmentException(
                    "Patient already has an overlapping appointment scheduled.");
        }

        checkDoctorAvailability(request.getDoctorId(), request.getFromDate(), request.getToDate());
        if (appointmentRepository.existsOverlappingAppointmentForDoctor(request.getDoctorId(), request.getFromDate(),
                request.getToDate())) {
            throw new DoctorTimeSlotUnavailableException(
                    "Doctor already has an overlapping appointment scheduled.");
        }

        Appointment newAppointment = Appointment.builder()
                .patientId(request.getPatientId())
                .doctorId(request.getDoctorId())
                .appointmentType(request.getAppointmentType())
                .description(request.getDescription())
                .fromDate(request.getFromDate())
                .toDate(request.getToDate())
                .appointmentStatus(AppointmentStatus.WAITING)
                .build();

        return appointmentRepository.save(newAppointment);
    }

    private void checkDoctorAvailability(UUID doctorId, OffsetDateTime from, OffsetDateTime to) {

        List<Shift> shifts = shiftRepository.findByDoctorId(doctorId);

        if (shifts.isEmpty()) {
            throw new DoctorNotWorkingException("Doctor does not work on " + from.getDayOfWeek());
        }

        LocalTime requestStartTime = from.toLocalTime();
        LocalTime requestEndTime = to.toLocalTime();

        boolean withinShift = shifts.stream().anyMatch(shift -> !requestStartTime.isBefore(shift.getFromTime())
                && !requestEndTime.isAfter(shift.getToTime()));

        if (!withinShift) {
            throw new DoctorNotWorkingException(
                    "Requested time slot is outside the doctor's working shifts for the day.");
        }
    }

    @Override
    public ListResponse<AppointmentResponse> getAllAppointmentsByDoctorId(UUID doctorId, Pageable pageable) {
        // Validate doctor existence externally first? Optional, depends on
        // requirements.
        // doctorExternalService.findDoctorById(doctorId)
        // .orElseThrow(() -> new ResourceNotFoundException("Doctor",
        // doctorId.toString()));

        Page<Appointment> resultPage = appointmentRepository.findByDoctorId(doctorId, pageable); // This query remains
                                                                                                 // valid

        List<AppointmentResponse> appointmentResponses = resultPage.getContent().stream()
                .map(appointmentMapper::mapAppointmentToAppointmentResponse)
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
        Appointment existingAppointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", id.toString()));

        // Validate new times if they changed
        if (!request.getFromDate().equals(existingAppointment.getFromDate())
                || !request.getToDate().equals(existingAppointment.getToDate())) {
            if (request.getToDate().isBefore(request.getFromDate())) {
                throw new IllegalArgumentException("Appointment 'toDate' must be after 'fromDate'.");
            }
            // Re-check doctor availability and overlaps for the new time
            checkDoctorAvailability(existingAppointment.getDoctorId(), request.getFromDate(), request.getToDate());
            if (appointmentRepository.existsOverlappingAppointmentForDoctorExcludingSelf(
                    existingAppointment.getDoctorId(), request.getFromDate(), request.getToDate(), id)) {
                throw new DoctorTimeSlotUnavailableException(
                        "Doctor already has an overlapping appointment scheduled for the new time.");
            }
            if (appointmentRepository.existsOverlappingAppointmentForPatientExcludingSelf(
                    existingAppointment.getPatientId(), request.getFromDate(), request.getToDate(), id)) {
                throw new PatientHasExistingAppointmentException(
                        "Patient already has an overlapping appointment scheduled for the new time.");
            }
        }

        // Update fields
        existingAppointment.setAppointmentType(request.getAppointmentType());
        existingAppointment.setDescription(request.getDescription());
        existingAppointment.setFromDate(request.getFromDate());
        existingAppointment.setToDate(request.getToDate());
        existingAppointment.setAppointmentStatus(request.getAppointmentStatus());

        return appointmentRepository.save(existingAppointment);
    }

    @Override
    @Transactional
    public Appointment updateAppointmentStatus(UpdateAppointmentStatusRequest request) {
        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", request.getAppointmentId().toString()));

        // Validate doctor using the ID stored in the appointment
        if (!appointment.getDoctorId().equals(request.getDoctorId())) {
            throw new IllegalArgumentException(
                    "Provided doctorId does not match the doctor assigned to this appointment.");
        }
        // Optionally re-validate doctor existence externally
        // doctorExternalService.findDoctorById(request.getDoctorId())
        // .orElseThrow(() -> new ResourceNotFoundException("Doctor",
        // request.getDoctorId().toString()));

        appointment.setAppointmentStatus(request.getNewStatus());
        return appointmentRepository.save(appointment);
    }

    @Override
    @Transactional
    public void deleteAppointment(UUID id) {

        if (!appointmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Appointment", id.toString());
        }

        appointmentRepository.deleteById(id);
    }
}
