package com.ktpmn.appointment.repository;

import com.ktpmn.appointment.model.Staff; // Import your Staff model
import com.ktpmn.appointment.constant.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StaffRepository extends JpaRepository<Staff, UUID> {
    Page<Staff> findByRole(Role role, Pageable pageable);
    // Add custom query methods if needed

    Staff findByIdAndRole(UUID id, Role role);
}
