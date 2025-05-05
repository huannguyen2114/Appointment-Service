package com.ktpmn.appointment.repository;

import com.ktpmn.appointment.model.Shift; // Import your Shift model
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, UUID> {
    // List<Shift> findByStaffId(UUID doctorId);

    List<Shift> findByDoctorId(UUID doctorId);
}