package com.ktpmn.appointment.repository;

import com.ktpmn.appointment.model.Shift; // Import your Shift model
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, UUID> {
    // Add custom query methods if needed
    List<Shift> findByStaffId(UUID doctorId);
}