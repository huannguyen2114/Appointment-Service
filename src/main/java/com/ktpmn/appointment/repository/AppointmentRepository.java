package com.ktpmn.appointment.repository;

import com.ktpmn.appointment.constant.AppointmentStatus;
import com.ktpmn.appointment.model.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // Import Query
import org.springframework.data.repository.query.Param; // Import Param
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

        Page<Appointment> findByDoctorId(UUID doctorId, Pageable pageable);

        List<Appointment> findByPatientId(UUID patientId); // Keep this if needed

        // Check for overlapping appointments for a specific patient
        @Query("SELECT CASE WHEN COUNT(a) > 0 THEN TRUE ELSE FALSE END " +
                        "FROM Appointment a " +
                        "WHERE a.patientId = :patientId " +
                        "AND a.fromDate < :toDate " + // Existing starts before new ends
                        "AND a.toDate > :fromDate") // Existing ends after new starts
        boolean existsOverlappingAppointmentForPatient(@Param("patientId") UUID patientId,
                        @Param("fromDate") OffsetDateTime fromDate,
                        @Param("toDate") OffsetDateTime toDate);

        // Check for overlapping appointments for a specific doctor
        @Query("SELECT CASE WHEN COUNT(a) > 0 THEN TRUE ELSE FALSE END " +
                        "FROM Appointment a " +
                        "WHERE a.doctorId = :doctorId " +
                        "AND a.fromDate < :toDate " +
                        "AND a.toDate > :fromDate")
        boolean existsOverlappingAppointmentForDoctor(@Param("doctorId") UUID doctorId,
                        @Param("fromDate") OffsetDateTime fromDate,
                        @Param("toDate") OffsetDateTime toDate);

        // Check for overlapping appointments for a patient, excluding a specific
        // appointment (for updates)
        @Query("SELECT CASE WHEN COUNT(a) > 0 THEN TRUE ELSE FALSE END " +
                        "FROM Appointment a " +
                        "WHERE a.patientId = :patientId " +
                        "AND a.id <> :appointmentId " + // Exclude the appointment being updated
                        "AND a.fromDate < :toDate " +
                        "AND a.toDate > :fromDate")
        boolean existsOverlappingAppointmentForPatientExcludingSelf(@Param("patientId") UUID patientId,
                        @Param("fromDate") OffsetDateTime fromDate,
                        @Param("toDate") OffsetDateTime toDate,
                        @Param("appointmentId") UUID appointmentId);

        // Check for overlapping appointments for a doctor, excluding a specific
        // appointment (for updates)
        @Query("SELECT CASE WHEN COUNT(a) > 0 THEN TRUE ELSE FALSE END " +
                        "FROM Appointment a " +
                        "WHERE a.doctorId = :doctorId " +
                        "AND a.id <> :appointmentId " + // Exclude the appointment being updated
                        "AND a.fromDate < :toDate " +
                        "AND a.toDate > :fromDate")
        boolean existsOverlappingAppointmentForDoctorExcludingSelf(@Param("doctorId") UUID doctorId,
                        @Param("fromDate") OffsetDateTime fromDate,
                        @Param("toDate") OffsetDateTime toDate,
                        @Param("appointmentId") UUID appointmentId);

        // Find appointments whose fromDate is in the past and status is still pending
        @Query("SELECT a FROM Appointment a WHERE a.fromDate < :currentTime AND a.appointmentStatus IN :pendingStatuses")
        List<Appointment> findPastPendingAppointments(
                        @Param("currentTime") OffsetDateTime currentTime,
                        @Param("pendingStatuses") List<AppointmentStatus> pendingStatuses); // Use List for IN clause
}