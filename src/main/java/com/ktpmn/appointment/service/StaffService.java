package com.ktpmn.appointment.service;

import com.ktpmn.appointment.dto.request.CreateStaffRequest;
import com.ktpmn.appointment.model.Staff; // Import Staff model

import com.ktpmn.appointment.constant.Role;

import com.ktpmn.appointment.dto.response.ListResponse;

import com.ktpmn.appointment.dto.response.StaffResponse;

import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface StaffService {

    ListResponse<StaffResponse> getAllDoctors(Pageable pageable);

    Staff fingById(UUID id);

    Staff findByIdAndRole(UUID id, Role role);

    Staff createStaff(CreateStaffRequest request);
    // Add other staff-related service methods
}