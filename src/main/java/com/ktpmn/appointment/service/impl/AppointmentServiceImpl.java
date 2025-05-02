package com.ktpmn.appointment.service.impl;

import com.ktpmn.appointment.constant.AppointmentStatus;
import com.ktpmn.appointment.dto.request.CreateAppointmentRequest;
import com.ktpmn.appointment.dto.request.UpdateAppointmentRequest; // Import Update DTO
import com.ktpmn.appointment.dto.request.UpdateAppointmentStatusRequest; // Import new DTO
import com.ktpmn.appointment.exception.*;
import com.ktpmn.appointment.exception.DoctorAppointmentMismatchException; // Import custom exception
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.ktpmn.appointment.constant.Role;
import com.ktpmn.appointment.dto.request.AppointmentCreateRequest;
import com.ktpmn.appointment.dto.response.AppointmentCreateResponse;
import com.ktpmn.appointment.dto.response.AppointmentResponse;
import com.ktpmn.appointment.dto.response.ListAppointmentResponse;
import com.ktpmn.appointment.dto.response.ListResponse;
import com.ktpmn.appointment.mapper.AppointmentMapper;
import com.ktpmn.appointment.model.Patient;
import com.ktpmn.appointment.model.Staff;
import com.ktpmn.appointment.repository.PatientRepository;
import com.ktpmn.appointment.repository.StaffRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ktpmn.appointment.model.Appointment;
import com.ktpmn.appointment.model.Patient;
import com.ktpmn.appointment.model.Shift;
import com.ktpmn.appointment.model.Staff;
import com.ktpmn.appointment.repository.AppointmentRepository;
import com.ktpmn.appointment.repository.PatientRepository;
import com.ktpmn.appointment.repository.ShiftRepository;
import com.ktpmn.appointment.repository.StaffRepository;
import com.ktpmn.appointment.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID; // Import UUID
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppointmentServiceImpl implements AppointmentService {

    AppointmentRepository appointmentRepository;
    PatientRepository patientRepository;
    StaffRepository staffRepository;
    ShiftRepository shiftRepository;
    AppointmentMapper appointmentMapper;

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
    public ListResponse<AppointmentResponse> getAllAppointmentsByDoctorId(UUID doctorId, Pageable pageable) {

//        Staff doctor = staffRepository.findByIdAndRole(doctorId, Role.DOCTOR);
//
//        if (doctor == null) {
//            throw
//        }

        Page<Appointment> result = appointmentRepository.findByDoctorId(doctorId, pageable);

//        System.out.println("cascascascsacascsac");

        ListResponse<AppointmentResponse> response = appointmentMapper.toListAppointmentResponse(result);

        return response;
    }

    @Override
    @Transactional
    public AppointmentCreateResponse createAppointment(AppointmentCreateRequest appointment) {

        Staff doctor = staffRepository.findById(appointment.getDoctorId()).orElse(null);

        if (doctor == null) {
            throw new IllegalArgumentException("Doctor not found");
        }

        Patient patient = patientRepository.findById(appointment.getPatientId()).orElse(null);
        if (patient == null) {
            throw new IllegalArgumentException("Patient not found");
        }


        Appointment result = appointmentRepository.save(Appointment.builder()
                .doctor(doctor)
                .patient(patient)
                .build());

        return appointmentMapper.toAppointmentCreateResponse(result);

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

    @Override
    @Transactional
    public Appointment updateAppointmentStatus(UpdateAppointmentStatusRequest request) {
        // Fetch the appointment
        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", request.getAppointmentId().toString()));

        // Fetch the doctor (optional, could just check the ID)
        // Staff doctor = staffRepository.findById(request.getDoctorId())
        // .orElseThrow(() -> new ResourceNotFoundException("Staff (Doctor)",
        // request.getDoctorId().toString()));

        // Verify the doctor assigned to the appointment matches the request
        if (!appointment.getDoctor().getId().equals(request.getDoctorId())) {
            throw new DoctorAppointmentMismatchException(
                    "Doctor with ID " + request.getDoctorId() + " is not assigned to appointment "
                            + request.getAppointmentId());
        }

        // Update the status
        appointment.setAppointmentStatus(request.getNewStatus());

        // Save and return the updated appointment
        return appointmentRepository.save(appointment);
    }

    private boolean isOverlapping(OffsetDateTime start1, OffsetDateTime end1, OffsetDateTime start2,
            OffsetDateTime end2) {
        return start1.isBefore(end2) && end1.isAfter(start2);
    }
}
