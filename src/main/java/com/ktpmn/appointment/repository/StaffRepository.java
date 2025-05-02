package com.ktpmn.appointment.repository;

import com.ktpmn.appointment.constant.Role;
import com.ktpmn.appointment.model.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StaffRepository extends JpaRepository<Staff, UUID> {
    Page<Staff> findByRole(Role role, Pageable pageable);

    Staff findByIdAndRole(UUID id, Role role);
}
