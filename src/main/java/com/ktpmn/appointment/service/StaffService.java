package com.ktpmn.appointment.service;


import com.ktpmn.appointment.constant.Role;
import com.ktpmn.appointment.dto.request.StaffCreateRequest;
import com.ktpmn.appointment.dto.response.ListResponse;
import com.ktpmn.appointment.dto.response.StaffCreateResponse;
import com.ktpmn.appointment.dto.response.StaffResponse;
import com.ktpmn.appointment.model.Staff;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

public interface StaffService {
    StaffCreateResponse createStaff(StaffCreateRequest request);

    ListResponse<StaffResponse> getAllDoctors(Pageable pageable);

    Staff fingById(UUID id);

    Staff findByIdAndRole(UUID id, Role role);

}
