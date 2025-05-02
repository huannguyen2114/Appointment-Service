package com.ktpmn.appointment.service.impl;

import com.ktpmn.appointment.constant.Role;
import com.ktpmn.appointment.dto.request.StaffCreateRequest;
import com.ktpmn.appointment.dto.response.ListResponse;
import com.ktpmn.appointment.dto.response.StaffCreateResponse;
import com.ktpmn.appointment.dto.response.StaffResponse;
import com.ktpmn.appointment.mapper.StaffMapper;
import com.ktpmn.appointment.model.Staff;
import com.ktpmn.appointment.repository.StaffRepository;
import com.ktpmn.appointment.service.StaffService;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StaffServiceImpl implements StaffService {
    StaffRepository staffRepository;
    StaffMapper staffMapper;

    @Override
    @Transactional
    public StaffCreateResponse createStaff(StaffCreateRequest request) {

        Staff result = staffRepository.save(staffMapper.toStaff(request));

        return staffMapper.toStaffCreateResponse(result);

    }

    @Override
    public ListResponse<StaffResponse> getAllDoctors(Pageable pageable) {
//        List<StaffResponse> responses = staffRepository.findByRole(Role.DOCTOR, pageable)
//                .stream()
//                .map(staffMapper::toStaffResponse)
//                .toList();

        Page<Staff> result = staffRepository.findByRole(Role.DOCTOR, pageable);
        ListResponse<StaffResponse> response = staffMapper.toListStaffResponse(result);
        return response;
    }

    @Override
    public Staff fingById(UUID id) {
        return staffRepository.findById(id).orElse(null);
    }

    @Override
    public Staff findByIdAndRole(UUID id, Role role) {
        return staffRepository.findByIdAndRole(id, role);
    }
}
