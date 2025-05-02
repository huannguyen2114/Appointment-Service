package com.ktpmn.appointment.repository;

import com.ktpmn.appointment.constant.AppointmentStatus; // Import AppointmentStatus
import com.ktpmn.appointment.model.Appointment;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    Page<Appointment> findByDoctorId(UUID doctorId, Pageable pageable);

    List<Appointment> findByPatientIdAndFromDateBetween(UUID patientId, OffsetDateTime startOfDay,
            OffsetDateTime endOfDay);

    List<Appointment> findByDoctorIdAndFromDateBetween(UUID doctorId, OffsetDateTime startOfDay,
            OffsetDateTime endOfDay);

    @Query("""
            SELECT COUNT(a) > 0 FROM Appointment a WHERE a.doctor.id = :doctorId
            AND ((a.fromDate < :toDate AND a.toDate > :fromDate))
            """)
    boolean existsOverlappingAppointmentForDoctor(@Param("doctorId") UUID doctorId,
            @Param("fromDate") OffsetDateTime fromDate,
            @Param("toDate") OffsetDateTime toDate);

    @Query("""
            SELECT COUNT(a) > 0 FROM Appointment a WHERE a.patient.id = :patientId
            AND ((a.fromDate < :toDate AND a.toDate > :fromDate))
            """)

    boolean existsOverlappingAppointmentForPatient(@Param("patientId") UUID patientId,
            @Param("fromDate") OffsetDateTime fromDate,
            @Param("toDate") OffsetDateTime toDate);

    @Query("SELECT COUNT(a) > 0 FROM Appointment a WHERE a.doctor.id = :doctorId " +
            "AND a.id <> :excludeId " + // Exclude the appointment being updated
            "AND ((a.fromDate < :toDate AND a.toDate > :fromDate))")
    boolean existsOverlappingAppointmentForDoctorExcludingSelf(@Param("doctorId") UUID doctorId,
            @Param("fromDate") OffsetDateTime fromDate,
            @Param("toDate") OffsetDateTime toDate,
            @Param("excludeId") UUID excludeId); // Add excludeId parameter

    @Query("SELECT COUNT(a) > 0 FROM Appointment a WHERE a.patient.id = :patientId " +
            "AND a.id <> :excludeId " + // Exclude the appointment being updated
            "AND ((a.fromDate < :toDate AND a.toDate > :fromDate))")
    boolean existsOverlappingAppointmentForPatientExcludingSelf(@Param("patientId") UUID patientId,
            @Param("fromDate") OffsetDateTime fromDate,
            @Param("toDate") OffsetDateTime toDate,
            @Param("excludeId") UUID excludeId); // Add excludeId parameter

    // Find appointments whose fromDate is in the past and status is still pending
    @Query("SELECT a FROM Appointment a WHERE a.fromDate < :currentTime AND a.appointmentStatus IN :pendingStatuses")
    List<Appointment> findPastPendingAppointments(
            @Param("currentTime") OffsetDateTime currentTime,
            @Param("pendingStatuses") List<AppointmentStatus> pendingStatuses); // Use List for IN clause
}